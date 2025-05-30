package com.yscorp.withpush.messagesystem.service

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.dto.domain.ChannelId
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.session.Session
import org.springframework.session.SessionRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.concurrent.TimeUnit

@Service
class SessionService(
    httpSessionRepository: SessionRepository<out Session?>,
    stringRedisTemplate: StringRedisTemplate
) {
    private val httpSessionRepository: SessionRepository<out Session> =
        httpSessionRepository
    private val stringRedisTemplate: StringRedisTemplate = stringRedisTemplate
    private val TTL: Long = 300

    val username: String
        get() {
            val authentication: Authentication =
                SecurityContextHolder.getContext().getAuthentication()
            return authentication.name
        }

    fun getOnlineParticipantUserIds(channelId: ChannelId, userIds: List<UserId>): List<UserId?> {
        val channelIdKeys = userIds.stream().map<String> { userId: UserId -> this.buildChannelIdKey(userId) }.toList()
        try {
            val channelIds  = stringRedisTemplate.opsForValue().multiGet(channelIdKeys)

            if (channelIds != null) {
                val onlineParticipantUserIds: MutableList<UserId> = ArrayList(userIds.size)
                val chId: String = channelId.id.toString()
                for (idx in userIds.indices) {
                    val value = channelIds[idx]

                    if (value != null && value == chId) {
                        onlineParticipantUserIds.add(userIds[idx])
                    }
                }
                return onlineParticipantUserIds
            }
        } catch (ex: Exception) {
            log.error("Redis mget failed. key: {}, cause: {}", channelIdKeys, ex.message)
        }
        return emptyList<UserId>()
    }

    fun setActiveChannel(userId: UserId, channelId: ChannelId): Boolean {
        val channelIdKey = buildChannelIdKey(userId)
        try {
            stringRedisTemplate
                .opsForValue()
                .set(channelIdKey, channelId.id.toString(), TTL, TimeUnit.SECONDS)
            return true
        } catch (ex: Exception) {
            log.error("Redis set failed. key: {}, channelId: {}", channelIdKey, channelId)
            return false
        }
    }

    fun removeActiveChannel(userId: UserId): Boolean {
        val channelIdKey = buildChannelIdKey(userId)
        try {
            stringRedisTemplate.delete(channelIdKey)
            return true
        } catch (ex: Exception) {
            log.error("Redis delete failed. key: {}", channelIdKey)
            return false
        }
    }

    fun refreshTTL(userId: UserId, httpSessionId: String?) {
        val channelIdKey = buildChannelIdKey(userId)
        try {
            val httpSession: Session = httpSessionRepository.findById(httpSessionId)
            if (httpSession != null) {
                httpSession.lastAccessedTime = Instant.now()
                stringRedisTemplate.expire(channelIdKey, TTL, TimeUnit.SECONDS)
            }
        } catch (ex: Exception) {
            log.error("Redis expire failed. key: {}", channelIdKey)
        }
    }

    private fun buildChannelIdKey(userId: UserId): String {
        val NAMESPACE = "message:user"

        return "%s:%d:%s".formatted(NAMESPACE, userId.id, IdKey.CHANNEL_ID.value)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SessionService::class.java)
    }
}
