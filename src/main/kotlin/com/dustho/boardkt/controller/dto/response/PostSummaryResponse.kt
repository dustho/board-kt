package com.dustho.boardkt.controller.dto.response

import java.time.LocalDateTime

data class PostSummaryResponse(
  val id: Long,
  val title: String,
  val createdBy: String,
  val createdAt: LocalDateTime,
)
