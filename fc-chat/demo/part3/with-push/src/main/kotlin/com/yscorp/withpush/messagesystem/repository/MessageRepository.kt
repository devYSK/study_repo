package com.yscorp.withpush.messagesystem.repository

import com.yscorp.withpush.messagesystem.entity.MessageEntity
import org.springframework.data.jpa.repository.JpaRepository


@org.springframework.stereotype.Repository
interface MessageRepository : JpaRepository<MessageEntity, Long>
