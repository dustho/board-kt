package com.dustho.boardkt.domain

import com.dustho.boardkt.exception.CommentNotUpdatableException
import com.dustho.boardkt.service.dto.request.CommentUpdateRequestDto
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Comment(
  content: String,
  post: Post,
  createdBy: String,
) : BaseEntity(createdBy) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0

  var content: String = content
    protected set

  @ManyToOne(fetch = FetchType.LAZY)
  var post: Post = post
    protected set

  fun update(commentUpdateRequestDto: CommentUpdateRequestDto) {
    if (createdBy != commentUpdateRequestDto.updatedBy) {
      throw CommentNotUpdatableException()
    }
    content = commentUpdateRequestDto.content
    super.updateBaseProps(commentUpdateRequestDto.updatedBy)
  }
}
