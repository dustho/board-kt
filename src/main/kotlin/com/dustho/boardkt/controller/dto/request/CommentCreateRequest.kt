package com.dustho.boardkt.controller.dto.request

import com.dustho.boardkt.service.dto.request.CommentCreateRequestDto

data class CommentCreateRequest(
  val content: String,
  val createdBy: String,
)

fun CommentCreateRequest.toDto() =
  CommentCreateRequestDto(
    content = content,
    createdBy = createdBy,
  )
