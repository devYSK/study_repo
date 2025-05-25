package com.yscorp.withpush.messagesystem.service

import net.prostars.messagesystem.constant.ResultType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.util.Pair
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Collectors

@Service
class ChannelService(
    private val sessionService: SessionService,
    private val userConnectionService: UserConnectionService,
    channelRepository: ChannelRepository,
    userChannelRepository: UserChannelRepository
) {
    private val channelRepository: ChannelRepository = channelRepository
    private val userChannelRepository: UserChannelRepository = userChannelRepository

    fun getInviteCode(channelId: ChannelId): Optional<InviteCode> {
        val inviteCode: Optional<InviteCode> =
            channelRepository
                .findChannelInviteCodeByChannelId(channelId.id())
                .map { projection -> InviteCode(projection.getInviteCode()) }
        if (inviteCode.isEmpty()) {
            log.warn("Invite code is not exist. channelId: {}", channelId)
        }
        return inviteCode
    }

    fun isJoined(channelId: ChannelId, userId: UserId): Boolean {
        return userChannelRepository.existsByUserIdAndChannelId(userId.id(), channelId.id())
    }

    fun getParticipantIds(channelId: ChannelId): List<UserId> {
        return userChannelRepository.findUserIdsByChannelId(channelId.id()).stream()
            .map { userId -> UserId(userId.getUserId()) }
            .toList()
    }

    fun getOnlineParticipantIds(channelId: ChannelId, userIds: List<UserId?>): List<UserId?> {
        return sessionService.getOnlineParticipantUserIds(channelId, userIds)
    }

    fun getChannel(inviteCode: InviteCode): Optional<Channel> {
        return channelRepository
            .findChannelByInviteCode(inviteCode.code())
            .map { projection ->
                Channel(
                    ChannelId(projection.getChannelId()),
                    projection.getTitle(),
                    projection.getHeadCount()
                )
            }
    }

    fun getChannels(userId: UserId): List<Channel> {
        return userChannelRepository.findChannelsByUserId(userId.id()).stream()
            .map { projection ->
                Channel(
                    ChannelId(projection.getChannelId()),
                    projection.getTitle(),
                    projection.getHeadCount()
                )
            }
            .toList()
    }

    @Transactional
    fun create(
        senderUserId: UserId, participantIds: List<UserId>, title: String?
    ): Pair<Optional<Channel>, ResultType> {
        if (title == null || title.isEmpty()) {
            log.warn("Invalid args : title is empty.")
            return Pair.of<Optional<Channel>, ResultType>(Optional.empty<Channel>(), ResultType.INVALID_ARGS)
        }

        val headCount = participantIds.size + 1
        if (headCount > LIMIT_HEAD_COUNT) {
            log.warn(
                "Over limit of channel. senderUserId: {}, participantIds count={}, title={}",
                senderUserId,
                participantIds.size,
                title
            )
            return Pair.of<Optional<Channel>, ResultType>(Optional.empty<Channel>(), ResultType.OVER_LIMIT)
        }

        if (userConnectionService.countConnectionStatus(
                senderUserId, participantIds, UserConnectionStatus.ACCEPTED
            )
            !== participantIds.size
        ) {
            log.warn("Included unconnected user. participantIds: {}", participantIds)
            return Pair.of<Optional<Channel>, ResultType>(Optional.empty<Channel>(), ResultType.NOT_ALLOWED)
        }

        try {
            val channelEntity: ChannelEntity = channelRepository.save(ChannelEntity(title, headCount))
            val channelId: Long = channelEntity.getChannelId()
            val userChannelEntities: MutableList<UserChannelEntity> =
                participantIds.stream()
                    .map<Any> { participantId: UserId -> UserChannelEntity(participantId.id(), channelId, 0) }
                    .collect(Collectors.toList<Any>())
            userChannelEntities.add(UserChannelEntity(senderUserId.id(), channelId, 0))
            userChannelRepository.saveAll(userChannelEntities)
            val channel: Channel = Channel(ChannelId(channelId), title, headCount)
            return Pair.of<Optional<Channel>, ResultType>(Optional.of<Channel>(channel), ResultType.SUCCESS)
        } catch (ex: Exception) {
            log.error("Create failed. cause: {}", ex.message)
            throw ex
        }
    }

    @Transactional
    fun join(inviteCode: InviteCode, userId: UserId): Pair<Optional<Channel>, ResultType> {
        val ch: Optional<Channel> = getChannel(inviteCode)
        if (ch.isEmpty()) {
            return Pair.of<Optional<Channel>, ResultType>(Optional.empty<Channel>(), ResultType.NOT_FOUND)
        }
        val channel: Channel = ch.get()

        if (isJoined(channel.channelId(), userId)) {
            return Pair.of<Optional<Channel>, ResultType>(Optional.empty<Channel>(), ResultType.ALREADY_JOINED)
        } else if (channel.headCount() >= LIMIT_HEAD_COUNT) {
            return Pair.of<Optional<Channel>, ResultType>(Optional.empty<Channel>(), ResultType.OVER_LIMIT)
        }

        val channelEntity: ChannelEntity =
            channelRepository
                .findForUpdateByChannelId(channel.channelId().id())
                .orElseThrow { EntityNotFoundException("Invalid channelId: " + channel.channelId()) }
        if (channelEntity.getHeadCount() < LIMIT_HEAD_COUNT) {
            channelEntity.setHeadCount(channelEntity.getHeadCount() + 1)
            userChannelRepository.save(UserChannelEntity(userId.id(), channel.channelId().id(), 0))
        }
        return Pair.of<Optional<Channel>, ResultType>(Optional.of<Channel>(channel), ResultType.SUCCESS)
    }

    fun enter(channelId: ChannelId, userId: UserId): Pair<Optional<String>, ResultType> {
        if (!isJoined(channelId, userId)) {
            log.warn(
                "Enter channel failed. User not joined the channel. channelId: {}, userId: {}",
                channelId,
                userId
            )
            return Pair.of<Optional<String>, ResultType>(Optional.empty<String>(), ResultType.NOT_JOINED)
        }

        val title: Optional<String> =
            channelRepository
                .findChannelTitleByChannelId(channelId.id())
                .map(ChannelTitleProjection::getTitle)
        if (title.isEmpty) {
            log.warn(
                "Enter channel failed. Channel does not exist. channelId: {}, userId: {}",
                channelId,
                userId
            )
            return Pair.of<Optional<String>, ResultType>(Optional.empty<String>(), ResultType.NOT_FOUND)
        }

        if (sessionService.setActiveChannel(userId, channelId)) {
            return Pair.of<Optional<String>, ResultType>(title, ResultType.SUCCESS)
        }

        log.error("Enter channel failed. channelId: {}, userId: {}", channelId, userId)
        return Pair.of<Optional<String>, ResultType>(Optional.empty<String>(), ResultType.FAILED)
    }

    fun leave(userId: UserId): Boolean {
        return sessionService.removeActiveChannel(userId)
    }

    @Transactional
    fun quit(channelId: ChannelId, userId: UserId): ResultType {
        if (!isJoined(channelId, userId)) {
            return ResultType.NOT_JOINED
        }

        val channelEntity: ChannelEntity =
            channelRepository
                .findForUpdateByChannelId(channelId.id())
                .orElseThrow { EntityNotFoundException("Invalid channelId: $channelId") }
        if (channelEntity.getHeadCount() > 0) {
            channelEntity.setHeadCount(channelEntity.getHeadCount() - 1)
        } else {
            log.error("Count is already zero. channelId: {}, userId: {}", channelId, userId)
        }

        userChannelRepository.deleteByUserIdAndChannelId(userId.id(), channelId.id())
        return ResultType.SUCCESS
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChannelService::class.java)
        private const val LIMIT_HEAD_COUNT = 100
    }
}
