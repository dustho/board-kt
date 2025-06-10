package com.dustho.boardkt.controller.dto.request

import java.time.LocalDateTime

data class PostCreateRequest(
  val title: String,
  val content: String,
  val createdBy: String,
  val createdAt: LocalDateTime,
)
