package com.dustho.boardkt.controller.dto.request

import com.dustho.boardkt.service.dto.request.CommentCreateRequestDto
import com.dustho.boardkt.service.dto.request.CommentUpdateRequestDto

data class CommentUpdateRequest(
  val content: String,
  val updatedBy: String,
)

fun CommentUpdateRequest.toDto() = CommentUpdateRequestDto(
  content = content,
  updatedBy = updatedBy
)
