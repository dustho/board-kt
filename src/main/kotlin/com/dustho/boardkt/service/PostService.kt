package com.dustho.boardkt.service

import com.dustho.boardkt.domain.Post
import com.dustho.boardkt.exception.PostNotDeletableException
import com.dustho.boardkt.exception.PostNotFoundException
import com.dustho.boardkt.repository.PostRepository
import com.dustho.boardkt.service.dto.PostCreateRequestDto
import com.dustho.boardkt.service.dto.PostUpdateRequestDto
import com.dustho.boardkt.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
  private val postRepository: PostRepository,
) {

  @Transactional
  fun createPost(requestDto: PostCreateRequestDto): Long {
    return postRepository.save(requestDto.toEntity()).id
  }

  @Transactional
  fun updatePost(savedPostId: Long, postUpdateRequestDto: PostUpdateRequestDto): Long {
    val savedPost = postRepository.findByIdOrNull(savedPostId) ?: throw PostNotFoundException()
    savedPost.update(postUpdateRequestDto)
    return savedPost.id
  }

  @Transactional
  fun deletePost(savedPostId: Long, deletedBy: String): Long {
    val savedPost = postRepository.findByIdOrNull(savedPostId) ?: throw PostNotFoundException()
    if (savedPost.createdBy != deletedBy) throw PostNotDeletableException()
    postRepository.delete(savedPost)
    return savedPost.id
  }

}
