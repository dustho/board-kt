package com.dustho.boardkt.service.dto.request

import com.dustho.boardkt.domain.Comment
import com.dustho.boardkt.domain.Post

data class CommentCreateRequestDto(
  val content: String,
  val createdBy: String,
)

fun CommentCreateRequestDto.toEntity(post: Post) = Comment(content = content, createdBy = createdBy, post = post)
