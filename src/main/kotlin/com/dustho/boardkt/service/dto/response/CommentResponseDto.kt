package com.dustho.boardkt.service.dto.response

import com.dustho.boardkt.controller.dto.response.CommentResponse
import com.dustho.boardkt.domain.Comment
import java.time.LocalDateTime

data class CommentResponseDto(
  val id: Long,
  val content: String,
  val createdBy: String,
  val createdAt: LocalDateTime,
)

fun Comment.toResponseDto() =
  CommentResponseDto(
    id = this.id,
    content = this.content,
    createdBy = this.createdBy,
    createdAt = this.createdAt,
  )

fun CommentResponseDto.toResponse() =
  CommentResponse(
    id = this.id,
    content = this.content,
    createdBy = this.createdBy,
    createdAt = this.createdAt,
  )
