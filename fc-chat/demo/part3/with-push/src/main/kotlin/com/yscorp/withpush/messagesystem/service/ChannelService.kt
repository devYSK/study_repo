package com.yscorp.withpush.messagesystem.service

import com.yscorp.withpush.messagesystem.constant.ResultType
import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus
import com.yscorp.withpush.messagesystem.dto.domain.Channel
import com.yscorp.withpush.messagesystem.dto.domain.ChannelId
import com.yscorp.withpush.messagesystem.dto.domain.InviteCode
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.entity.ChannelEntity
import com.yscorp.withpush.messagesystem.entity.UserChannelEntity
import com.yscorp.withpush.messagesystem.repository.ChannelRepository
import com.yscorp.withpush.messagesystem.repository.UserChannelRepository
import jakarta.persistence.EntityNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ChannelService(
    private val sessionService: SessionService,
    private val userConnectionService: UserConnectionService,
    private val channelRepository: ChannelRepository,
    private val userChannelRepository: UserChannelRepository
) {

    fun getInviteCode(channelId: ChannelId): InviteCode? {
        val projection = channelRepository.findChannelInviteCodeByChannelId(channelId.id)
        if (projection == null) {
            log.warn("Invite code is not exist. channelId: {}", channelId)
            return null
        }
        return InviteCode(projection.inviteCode)
    }

    fun isJoined(channelId: ChannelId, userId: UserId): Boolean {
        return userChannelRepository.existsByUserIdAndChannelId(userId.id, channelId.id)
    }

    fun getParticipantIds(channelId: ChannelId): List<UserId> {
        return userChannelRepository.findUserIdsByChannelId(channelId.id)
            .map { UserId(it.userId) }
    }

    fun getOnlineParticipantIds(channelId: ChannelId, userIds: List<UserId>): List<UserId?> {
        return sessionService.getOnlineParticipantUserIds(channelId, userIds)
    }

    fun getChannel(inviteCode: InviteCode): Channel? {
        return channelRepository.findChannelByInviteCode(inviteCode.code)?.let {
            Channel(ChannelId(it.channelId), it.title, it.headCount)
        }
    }

    fun getChannels(userId: UserId): List<Channel> {
        return userChannelRepository.findChannelsByUserId(userId.id)
            .map { Channel(ChannelId(it.channelId), it.title, it.headCount) }
    }

    @Transactional
    fun create(senderUserId: UserId, participantIds: List<UserId>, title: String?): Pair<Channel?, ResultType> {
        if (title.isNullOrBlank()) {
            log.warn("Invalid args : title is empty.")
            return null to ResultType.INVALID_ARGS
        }

        val headCount = participantIds.size + 1
        if (headCount > LIMIT_HEAD_COUNT) {
            log.warn(
                "Over limit of channel. senderUserId: {}, participantIds count={}, title={}",
                senderUserId, participantIds.size, title
            )
            return null to ResultType.OVER_LIMIT
        }

        if (userConnectionService.countConnectionStatus(senderUserId, participantIds, UserConnectionStatus.ACCEPTED)
            != participantIds.size.toLong()
        ) {
            log.warn("Included unconnected user. participantIds: {}", participantIds)
            return null to ResultType.NOT_ALLOWED
        }

        return try {
            val channelEntity = channelRepository.save(ChannelEntity(title, headCount))
            val channelId = channelEntity.channelId ?: throw IllegalStateException("Channel ID is null after save.")

            val userChannelEntities = participantIds.map {
                UserChannelEntity(it.id, channelId, 0)
            }.toMutableList().apply {
                add(UserChannelEntity(senderUserId.id, channelId, 0))
            }

            userChannelRepository.saveAll(userChannelEntities)
            Channel(ChannelId(channelId), title, headCount) to ResultType.SUCCESS
        } catch (ex: Exception) {
            log.error("Create failed. cause: {}", ex.message)
            throw ex
        }
    }

    @Transactional
    fun join(inviteCode: InviteCode, userId: UserId): Pair<Channel?, ResultType> {
        val channel = getChannel(inviteCode) ?: return null to ResultType.NOT_FOUND

        if (isJoined(channel.channelId, userId)) {
            return null to ResultType.ALREADY_JOINED
        }

        if (channel.headCount >= LIMIT_HEAD_COUNT) {
            return null to ResultType.OVER_LIMIT
        }

        val channelEntity = channelRepository.findForUpdateByChannelId(channel.channelId.id)
            ?: throw EntityNotFoundException("Invalid channelId: ${channel.channelId}")

        if (channelEntity.headCount < LIMIT_HEAD_COUNT) {
            channelEntity.headCount += 1
            userChannelRepository.save(UserChannelEntity(userId.id, channel.channelId.id, 0))
        }

        return channel to ResultType.SUCCESS
    }

    fun enter(channelId: ChannelId, userId: UserId): Pair<String?, ResultType> {
        if (!isJoined(channelId, userId)) {
            log.warn("Enter channel failed. User not joined. channelId: {}, userId: {}", channelId, userId)
            return null to ResultType.NOT_JOINED
        }

        val title = channelRepository.findChannelTitleByChannelId(channelId.id)?.title
        if (title == null) {
            log.warn("Enter channel failed. Channel not found. channelId: {}, userId: {}", channelId, userId)
            return null to ResultType.NOT_FOUND
        }

        return if (sessionService.setActiveChannel(userId, channelId)) {
            title to ResultType.SUCCESS
        } else {
            log.error("Enter channel failed. channelId: {}, userId: {}", channelId, userId)
            null to ResultType.FAILED
        }
    }

    fun leave(userId: UserId): Boolean {
        return sessionService.removeActiveChannel(userId)
    }

    @Transactional
    fun quit(channelId: ChannelId, userId: UserId): ResultType {
        if (!isJoined(channelId, userId)) {
            return ResultType.NOT_JOINED
        }

        val channelEntity = channelRepository.findForUpdateByChannelId(channelId.id)
            ?: throw EntityNotFoundException("Invalid channelId: $channelId")

        if (channelEntity.headCount > 0) {
            channelEntity.headCount -= 1
        } else {
            log.error("Count is already zero. channelId: {}, userId: {}", channelId, userId)
        }

        userChannelRepository.deleteByUserIdAndChannelId(userId.id, channelId.id)
        return ResultType.SUCCESS
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChannelService::class.java)
        private const val LIMIT_HEAD_COUNT = 100
    }
}
