package com.yscorp.withpush.messagesystem.service

import net.prostars.messagesystem.constant.UserConnectionStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.util.Pair
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Predicate
import java.util.stream.Stream

@Service
class UserConnectionService(
    private val userService: UserService,
    private val userConnectionLimitService: UserConnectionLimitService,
    userConnectionRepository: UserConnectionRepository
) {
    private val userConnectionRepository: UserConnectionRepository = userConnectionRepository

    @Transactional(readOnly = true)
    fun getUsersByStatus(userId: UserId, status: UserConnectionStatus): List<User> {
        val usersA: List<UserIdUsernameInviterUserIdProjection> =
            userConnectionRepository.findByPartnerAUserIdAndStatus(userId.id(), status)
        val usersB: List<UserIdUsernameInviterUserIdProjection> =
            userConnectionRepository.findByPartnerBUserIdAndStatus(userId.id(), status)
        return if (status === UserConnectionStatus.ACCEPTED) {
            Stream.concat<UserIdUsernameInviterUserIdProjection>(usersA.stream(), usersB.stream())
                .map<Any> { item: UserIdUsernameInviterUserIdProjection ->
                    User(
                        UserId(item.getUserId()),
                        item.getUsername()
                    )
                }
                .toList()
        } else {
            Stream.concat<UserIdUsernameInviterUserIdProjection>(usersA.stream(), usersB.stream())
                .filter { item: UserIdUsernameInviterUserIdProjection -> !item.getInviterUserId().equals(userId.id()) }
                .map<Any> { item: UserIdUsernameInviterUserIdProjection ->
                    User(
                        UserId(item.getUserId()),
                        item.getUsername()
                    )
                }
                .toList()
        }
    }

    fun getStatus(inviterUserId: UserId, partnerUserId: UserId): UserConnectionStatus {
        return userConnectionRepository
            .findUserConnectionStatusByPartnerAUserIdAndPartnerBUserId(
                java.lang.Long.min(inviterUserId.id(), partnerUserId.id()),
                java.lang.Long.max(inviterUserId.id(), partnerUserId.id())
            )
            .map { status -> UserConnectionStatus.valueOf(status.getStatus()) }
            .orElse(UserConnectionStatus.NONE)
    }

    @Transactional(readOnly = true)
    fun countConnectionStatus(
        senderUserId: UserId, partnerUserIds: List<UserId>, status: UserConnectionStatus?
    ): Long {
        val ids: List<Long> = partnerUserIds.stream().map<Any>(UserId::id).toList()
        return (userConnectionRepository.countByPartnerAUserIdAndPartnerBUserIdInAndStatus(
            senderUserId.id(), ids, status
        )
            + userConnectionRepository.countByPartnerBUserIdAndPartnerAUserIdInAndStatus(
            senderUserId.id(), ids, status
        ))
    }

    fun invite(inviterUserId: UserId, inviteCode: InviteCode): Pair<Optional<UserId>, String> {
        val partner: Optional<User?>? = userService.getUser(inviteCode)
        if (partner!!.isEmpty()) {
            log.info("Invalid invite code. {}, from {}", inviteCode, inviterUserId)
            return Pair.of<Optional<UserId>, String>(Optional.empty<UserId>(), "Invalid invite code.")
        }

        val partnerUserId: UserId = partner.get().userId()
        val partnerUsername: String = partner.get().username()
        if (partnerUserId.equals(inviterUserId)) {
            return Pair.of<Optional<UserId>, String>(Optional.empty<UserId>(), "Can't self invite.")
        }

        val userConnectionStatus: UserConnectionStatus = getStatus(inviterUserId, partnerUserId)
        return when (userConnectionStatus) {
            NONE, DISCONNECTED -> {
                if (userService
                        .getConnectionCount(inviterUserId)
                        .filter { count -> count >= userConnectionLimitService.limitConnections }
                        .isPresent
                ) {
                    Pair.of<Optional<UserId>, String>(Optional.empty<UserId>(), "Connection limit reached.")
                }

                val inviterUsername = userService.getUsername(inviterUserId)
                if (inviterUsername!!.isEmpty) {
                    log.warn("InviteRequest failed.")
                    Pair.of<Optional<UserId>, String>(Optional.empty<UserId>(), "InviteRequest failed.")
                }
                try {
                    setStatus(inviterUserId, partnerUserId, UserConnectionStatus.PENDING)
                    Pair.of<Optional<UserId>, String>(Optional.of<UserId>(partnerUserId), inviterUsername.get())
                } catch (ex: Exception) {
                    log.error("Set pending failed. cause: {}", ex.message)
                    Pair.of<Optional<UserId>, String>(Optional.empty<UserId>(), "InviteRequest failed.")
                }
            }

            ACCEPTED -> Pair.of<Optional<UserId>, String>(
                Optional.of<UserId>(partnerUserId),
                "Already connected with $partnerUsername"
            )

            PENDING, REJECTED -> {
                log.info(
                    "{} invites {} but does not deliver the invitation request.",
                    inviterUserId,
                    partnerUsername
                )
                Pair.of<Optional<UserId>, String>(
                    Optional.of<UserId>(partnerUserId),
                    "Already invited to $partnerUsername"
                )
            }
        }
    }

    @Transactional
    fun accept(acceptorUserId: UserId, inviterUsername: String?): Pair<Optional<UserId>, String?> {
        val userId: Optional<UserId?>? = userService.getUserId(inviterUsername)
        if (userId!!.isEmpty()) {
            return Pair.of<Optional<UserId>, String?>(Optional.empty<UserId>(), "Invalid username.")
        }
        val inviterUserId: UserId = userId.get()

        if (acceptorUserId.equals(inviterUserId)) {
            return Pair.of<Optional<UserId>, String?>(Optional.empty<UserId>(), "Can't self accept.")
        }

        if (getInviterUserId(acceptorUserId, inviterUserId)
                .filter(Predicate<UserId> { invitationSenderUserId: UserId ->
                    invitationSenderUserId.equals(
                        inviterUserId
                    )
                })
                .isEmpty()
        ) {
            return Pair.of<Optional<UserId>, String?>(Optional.empty<UserId>(), "Invalid username.")
        }

        val userConnectionStatus: UserConnectionStatus = getStatus(inviterUserId, acceptorUserId)
        if (userConnectionStatus === UserConnectionStatus.ACCEPTED) {
            return Pair.of<Optional<UserId>, String?>(Optional.empty<UserId>(), "Already connected.")
        }
        if (userConnectionStatus !== UserConnectionStatus.PENDING) {
            return Pair.of<Optional<UserId>, String?>(Optional.empty<UserId>(), "Accept failed.")
        }

        val acceptorUsername = userService.getUsername(acceptorUserId)
        if (acceptorUsername!!.isEmpty) {
            log.error("Invalid userId. userId: {}", acceptorUserId)
            return Pair.of<Optional<UserId>, String?>(Optional.empty<UserId>(), "Accept failed.")
        }

        try {
            userConnectionLimitService.accept(acceptorUserId, inviterUserId)
            return Pair.of<Optional<UserId>, String?>(Optional.of<UserId>(inviterUserId), acceptorUsername.get())
        } catch (ex: IllegalStateException) {
            return Pair.of<Optional<UserId>, String?>(Optional.empty<UserId>(), ex.message)
        } catch (ex: Exception) {
            log.error("Accept failed. cause: {}", ex.message)
            return Pair.of<Optional<UserId>, String?>(Optional.empty<UserId>(), "Accept failed.")
        }
    }

    fun reject(senderUserId: UserId, inviterUsername: String): Pair<Boolean, String> {
        return userService
            .getUserId(inviterUsername)
            .filter { inviterUserId -> !inviterUserId.equals(senderUserId) }
            .filter { inviterUserId ->
                getInviterUserId(inviterUserId, senderUserId)
                    .filter(Predicate<UserId> { invitationSenderUserId: UserId ->
                        invitationSenderUserId.equals(
                            inviterUserId
                        )
                    })
                    .isPresent()
            }
            .filter { inviterUserId -> getStatus(inviterUserId, senderUserId) === UserConnectionStatus.PENDING }
            .map { inviterUserId ->
                try {
                    setStatus(inviterUserId, senderUserId, UserConnectionStatus.REJECTED)
                    return@map Pair.of<S, T>(true, inviterUsername)
                } catch (ex: Exception) {
                    log.error(
                        "Set rejected failed. cause: {}",
                        ex.message
                    )
                    return@map Pair.of<S, T>(false, "Reject failed.")
                }
            }
            .orElse(Pair.of<S, T>(false, "Reject failed."))
    }

    @Transactional
    fun disconnect(senderUserId: UserId, partnerUsername: String): Pair<Boolean, String> {
        return userService
            .getUserId(partnerUsername)
            .filter { partnerUserId -> !senderUserId.equals(partnerUserId) }
            .map { partnerUserId ->
                try {
                    val userConnectionStatus: UserConnectionStatus = getStatus(senderUserId, partnerUserId)
                    if (userConnectionStatus === UserConnectionStatus.ACCEPTED) {
                        userConnectionLimitService.disconnect(senderUserId, partnerUserId)
                        return@map Pair.of<S, T>(true, partnerUsername)
                    } else if (userConnectionStatus === UserConnectionStatus.REJECTED
                        && getInviterUserId(senderUserId, partnerUserId)
                            .filter(Predicate<UserId> { inviterUserId: UserId ->
                                inviterUserId.equals(
                                    partnerUserId
                                )
                            })
                            .isPresent()
                    ) {
                        setStatus(senderUserId, partnerUserId, UserConnectionStatus.DISCONNECTED)
                        return@map Pair.of<S, T>(true, partnerUsername)
                    }
                } catch (ex: Exception) {
                    log.error(
                        "Disconnect failed. cause: {}",
                        ex.message
                    )
                }
                Pair.of<S, T>(false, "Disconnect failed.")
            }
            .orElse(Pair.of<S, T>(false, "Disconnect failed."))
    }

    private fun getInviterUserId(partnerAUserId: UserId, partnerBUserId: UserId): Optional<UserId> {
        return userConnectionRepository
            .findInviterUserIdByPartnerAUserIdAndPartnerBUserId(
                java.lang.Long.min(partnerAUserId.id(), partnerBUserId.id()),
                java.lang.Long.max(partnerAUserId.id(), partnerBUserId.id())
            )
            .map { inviterUserId -> UserId(inviterUserId.getInviterUserId()) }
    }

    @Transactional
    private fun setStatus(
        inviterUserId: UserId, partnerUserId: UserId, userConnectionStatus: UserConnectionStatus
    ) {
        require(userConnectionStatus !== UserConnectionStatus.ACCEPTED) { "Can't set to accepted." }

        userConnectionRepository.save(
            UserConnectionEntity(
                java.lang.Long.min(inviterUserId.id(), partnerUserId.id()),
                java.lang.Long.max(inviterUserId.id(), partnerUserId.id()),
                userConnectionStatus,
                inviterUserId.id()
            )
        )
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(UserConnectionService::class.java)
    }
}
