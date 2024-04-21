package com.example.walletservice.wallet.domain

data class WalletEventMessage (
  val type: WalletEventMessageType,
  val payload: Map<String, Any?> = emptyMap(),
  val metadata: Map<String, Any?> = emptyMap()
)

enum class WalletEventMessageType (description: String) {
  SUCCESS("정산 성공")
}