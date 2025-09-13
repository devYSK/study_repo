package com.yscorp.mongobatch

class Pattern {
}

/**
 * 명령어 문자열을 분석하여 공격 패턴 라벨을 반환합니다.
 *
 * - "ssh" 또는 "telnet" 포함 시 Lateral_Movement (측면 이동)
 * - "sudo" 또는 "su " 포함 시 Privilege_Escalation (권한 상승)
 * - "history -c", "rm /var/log", "killall rsyslog" 포함 시 Defense_Evasion (방어 회피)
 * - 그 외는 UNKNOWN (알 수 없음)
 */
fun analyzeAttackPattern(command: String): String {
    return when {
        command.contains("ssh", ignoreCase = true) || command.contains("telnet", ignoreCase = true) -> "Lateral_Movement"
        command.contains("sudo", ignoreCase = true) || command.contains("su ", ignoreCase = true) -> "Privilege_Escalation"
        command.contains("history -c", ignoreCase = true) ||
            command.contains("rm /var/log", ignoreCase = true) ||
            command.contains("killall rsyslog", ignoreCase = true) -> "Defense_Evasion"
        else -> "UNKNOWN"
    }
}