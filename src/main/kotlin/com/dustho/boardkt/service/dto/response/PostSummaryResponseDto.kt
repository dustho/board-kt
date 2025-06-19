package com.dustho.boardkt.service.dto.response

import com.dustho.boardkt.controller.dto.response.PostSummaryResponse
import com.dustho.boardkt.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import java.time.LocalDateTime

data class PostSummaryResponseDto(
  val id: Long,
  val title: String,
  val createdBy: String,
  val createdAt: LocalDateTime,
  val firstTag: String?,
)

fun Page<Post>.toPostSummaryResponseDto() =
  PageImpl(
    content.map { it.toPostSummaryResponseDto() },
    pageable,
    totalElements,
  )

fun Post.toPostSummaryResponseDto() =
  PostSummaryResponseDto(
    id = this.id,
    title = this.title,
    createdAt = this.createdAt,
    createdBy = this.createdBy,
    firstTag =
      this.postTags
        .firstOrNull()
        ?.tag
        ?.name,
  )

fun Page<PostSummaryResponseDto>.toResponse() =
  PageImpl(
    content.map { it.toPostSummaryResponse() },
    pageable,
    totalElements,
  )

fun PostSummaryResponseDto.toPostSummaryResponse() =
  PostSummaryResponse(
    this.id,
    this.title,
    this.createdBy,
    this.createdAt,
  )
