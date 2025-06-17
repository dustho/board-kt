package com.dustho.boardkt.controller.dto.request

import com.dustho.boardkt.service.dto.request.PostUpdateRequestDto

data class PostUpdateRequest(
  val title: String,
  val content: String,
  val updatedBy: String,
  val tags: List<String>,
)

fun PostUpdateRequest.toDto() =
  PostUpdateRequestDto(
    title = this.title,
    content = this.content,
    updatedBy = this.updatedBy,
  )
