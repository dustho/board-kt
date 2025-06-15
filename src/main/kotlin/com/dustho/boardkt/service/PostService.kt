package com.dustho.boardkt.service

import com.dustho.boardkt.exception.PostNotDeletableException
import com.dustho.boardkt.exception.PostNotFoundException
import com.dustho.boardkt.repository.PostRepository
import com.dustho.boardkt.service.dto.request.PostCreateRequestDto
import com.dustho.boardkt.service.dto.request.PostSearchRequestDto
import com.dustho.boardkt.service.dto.request.PostUpdateRequestDto
import com.dustho.boardkt.service.dto.request.toEntity
import com.dustho.boardkt.service.dto.response.PostDetailResponseDto
import com.dustho.boardkt.service.dto.response.PostSummaryResponseDto
import com.dustho.boardkt.service.dto.response.toPostDetailResponseDto
import com.dustho.boardkt.service.dto.response.toPostSummaryResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
  private val postRepository: PostRepository,
) {
  @Transactional
  fun createPost(requestDto: PostCreateRequestDto): Long = postRepository.save(requestDto.toEntity()).id

  @Transactional
  fun updatePost(
    savedPostId: Long,
    postUpdateRequestDto: PostUpdateRequestDto,
  ): Long {
    val savedPost = postRepository.findByIdOrNull(savedPostId) ?: throw PostNotFoundException()
    savedPost.update(postUpdateRequestDto)
    return savedPost.id
  }

  @Transactional
  fun deletePost(
    savedPostId: Long,
    deletedBy: String,
  ): Long {
    val savedPost = postRepository.findByIdOrNull(savedPostId) ?: throw PostNotFoundException()
    if (savedPost.createdBy != deletedBy) throw PostNotDeletableException()
    postRepository.delete(savedPost)
    return savedPost.id
  }

  fun findPost(savedPostId: Long): PostDetailResponseDto =
    postRepository.findByIdOrNull(savedPostId)?.toPostDetailResponseDto() ?: throw PostNotFoundException()

  fun findPostPageBy(
    pageRequest: Pageable,
    postSearchRequest: PostSearchRequestDto,
  ): Page<PostSummaryResponseDto> = postRepository.findPageBy(pageRequest, postSearchRequest).toPostSummaryResponseDto()
}
