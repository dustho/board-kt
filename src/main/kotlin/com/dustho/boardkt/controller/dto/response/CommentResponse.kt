package com.dustho.boardkt.controller.dto.response

import java.time.LocalDateTime

data class CommentResponse(
  val id: Long,
  val content: String,
  val createdBy: String,
  val createdAt: LocalDateTime,
)
