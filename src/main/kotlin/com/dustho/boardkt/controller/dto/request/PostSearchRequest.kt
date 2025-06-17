package com.dustho.boardkt.controller.dto.request

import com.dustho.boardkt.service.dto.request.PostSearchRequestDto

data class PostSearchRequest(
  val title: String?,
  val content: String?,
  val tags: List<String>?,
)

fun PostSearchRequest.toDto() =
  PostSearchRequestDto(
    title = title,
    content = content,
  )
