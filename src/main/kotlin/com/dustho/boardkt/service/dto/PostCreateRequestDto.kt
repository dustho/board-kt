package com.dustho.boardkt.service.dto

import com.dustho.boardkt.domain.Post

data class PostCreateRequestDto (
  val title: String,
  val content: String,
  val createdBy: String,
)

fun PostCreateRequestDto.toEntity() = Post(
  title = title,
  content = content,
  createdBy = createdBy
)
