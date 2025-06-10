package com.dustho.boardkt.controller.dto.request

import java.time.LocalDateTime

data class PostUpdateRequest(
  val title: String,
  val content: String,
  val updatedBy: String,
  val updatedAt: LocalDateTime,
)
