package com.yscorp.withpush.messagesystem.repository

import net.prostars.messagesystem.entity.MessageEntity

@org.springframework.stereotype.Repository
interface MessageRepository : JpaRepository<MessageEntity?, Long?>
