package com.yscorp.mongobatch

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime


/**
 * 보안 로그 도큐먼트 엔티티입니다.
 *
 * @property id 문서 ID
 * @property attackerId 공격자 ID
 * @property command 실행된 명령어
 * @property timestamp 로그 기록 시간
 * @property label 공격 패턴 라벨 ("PENDING_ANALYSIS" 등)
 */
@Document(collection = "security_logs")
data class SecurityLog(
    @Id
    var id: String? = null,

    var attackerId: String? = null,

    var command: String = "",

    var timestamp: LocalDateTime = LocalDateTime.MIN,

    var label: String = "PENDING_ANALYSIS"
)