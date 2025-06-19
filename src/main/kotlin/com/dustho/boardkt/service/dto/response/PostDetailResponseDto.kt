package com.dustho.boardkt.service.dto.response

import com.dustho.boardkt.controller.dto.response.PostDetailResponse
import com.dustho.boardkt.domain.Post
import java.time.LocalDateTime

data class PostDetailResponseDto(
  val id: Long,
  val title: String,
  val content: String,
  val createdBy: String,
  val updatedBy: String?,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime?,
  val comments: List<CommentResponseDto>,
  val tags: List<String> = emptyList(),
)

fun Post.toPostDetailResponseDto(): PostDetailResponseDto =
  PostDetailResponseDto(
    id = this.id,
    title = this.title,
    content = this.content,
    createdBy = this.createdBy,
    createdAt = this.createdAt,
    updatedBy = this.updatedBy,
    updatedAt = this.updatedAt,
    comments = comments.map { it.toResponseDto() },
    tags = postTags.map { it.tag.name },
  )

fun PostDetailResponseDto.toResponse() =
  PostDetailResponse(
    id = this.id,
    title = this.title,
    content = this.content,
    createdBy = this.createdBy,
    createdAt = this.createdAt,
    updatedBy = this.updatedBy,
    updatedAt = this.updatedAt,
    comments = this.comments.map { it.toResponse() },
  )
