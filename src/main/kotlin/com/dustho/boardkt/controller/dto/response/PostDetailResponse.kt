package com.dustho.boardkt.controller.dto.response

import com.dustho.boardkt.controller.dto.request.CommentUpdateRequest
import java.time.LocalDateTime

data class PostDetailResponse(
  val id: Long,
  val title: String,
  val content: String,
  val createdBy: String,
  val updatedBy: String?,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime?,
  val comments: List<CommentResponse> = emptyList(),
)
