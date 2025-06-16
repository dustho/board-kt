package com.dustho.boardkt.controller

import com.dustho.boardkt.controller.dto.request.CommentCreateRequest
import com.dustho.boardkt.controller.dto.request.CommentUpdateRequest
import com.dustho.boardkt.controller.dto.request.toDto
import com.dustho.boardkt.service.CommentService
import jakarta.websocket.server.PathParam
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController(
  val commentService: CommentService
) {
  @PostMapping("/posts/{postId}/comments")
  fun createComment(
    @PathVariable postId: Long,
    @RequestBody request: CommentCreateRequest
  ): ResponseEntity<Long> = ResponseEntity.ok(commentService.createComment(postId, request.toDto()))

  @PutMapping("/comments/{commentId}")
  fun updateComment(
    @PathVariable commentId: Long,
    @RequestBody request: CommentUpdateRequest
  ): ResponseEntity<Long> = ResponseEntity.ok(commentService.updateComment(commentId, request.toDto()))

  @DeleteMapping("/comments/{commentId}")
  fun deleteComment(
    @PathVariable commentId: Long,
    @RequestParam deletedBy: String,
  ): ResponseEntity<Void> {
    commentService.deleteComment(commentId, deletedBy)
    return ResponseEntity.noContent().build()
  }
}
