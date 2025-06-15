package com.dustho.boardkt.controller.dto.request

import com.dustho.boardkt.service.dto.request.PostCreateRequestDto

data class PostCreateRequest(
  val title: String,
  val content: String,
  val createdBy: String,
)

fun PostCreateRequest.toDto() =
  PostCreateRequestDto(
    title = this.title,
    content = this.content,
    createdBy = this.createdBy,
  )
