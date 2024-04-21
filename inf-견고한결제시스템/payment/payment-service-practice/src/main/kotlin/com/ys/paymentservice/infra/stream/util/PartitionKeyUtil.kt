package com.ys.paymentservice.infra.stream.util

import org.springframework.stereotype.Component
import kotlin.math.abs

@Component
class PartitionKeyUtil {
  val PARTITION_KEY_COUNT = 6

  fun createPartitionKey(number: Int): Int {
    return abs(number) % PARTITION_KEY_COUNT
  }

}