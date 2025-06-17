package com.dustho.boardkt.service.dto.request

import com.dustho.boardkt.domain.Post

data class PostCreateRequestDto(
  val title: String,
  val content: String,
  val createdBy: String,
  val tags: List<String> = emptyList(),
)

fun PostCreateRequestDto.toEntity() =
  Post(
    title = title,
    content = content,
    createdBy = createdBy,
  )
