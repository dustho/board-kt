package com.dustho.boardkt.service

import com.dustho.boardkt.exception.CommentNotDeletableException
import com.dustho.boardkt.exception.CommentNotFoundException
import com.dustho.boardkt.exception.PostNotFoundException
import com.dustho.boardkt.repository.CommentRepository
import com.dustho.boardkt.repository.PostRepository
import com.dustho.boardkt.service.dto.request.CommentCreateRequestDto
import com.dustho.boardkt.service.dto.request.CommentUpdateRequestDto
import com.dustho.boardkt.service.dto.request.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
  val commentRepository: CommentRepository,
  val postRepository: PostRepository,
) {
  @Transactional
  fun createComment(postId: Long, commentCreateRequestDto: CommentCreateRequestDto): Long {
    val savedPost = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
    val savedComment = commentRepository.save(commentCreateRequestDto.toEntity(savedPost))
    return savedComment.id
  }

  @Transactional
  fun updateComment(commentId: Long, commentUpdateRequestDto: CommentUpdateRequestDto): Long {
    val savedComment = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
    savedComment.update(commentUpdateRequestDto)
    return savedComment.id
  }

  @Transactional
  fun deleteComment(commentId: Long, deletedBy: String) {
    val savedComment = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
    if (savedComment.createdBy != deletedBy) {
      throw CommentNotDeletableException()
    }
    commentRepository.delete(savedComment)
  }
}
