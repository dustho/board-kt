package com.dustho.boardkt.controller

import com.dustho.boardkt.controller.dto.request.PostCreateRequest
import com.dustho.boardkt.controller.dto.response.PostDetailResponse
import com.dustho.boardkt.controller.dto.request.PostSearchRequest
import com.dustho.boardkt.controller.dto.request.PostUpdateRequest
import com.dustho.boardkt.controller.dto.request.toDto
import com.dustho.boardkt.controller.dto.response.PostSummaryResponse
import com.dustho.boardkt.repository.PostRepository
import com.dustho.boardkt.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/posts")
class PostController(
  private val postService: PostService,
) {

  @PostMapping
  fun createPost(
    @RequestBody request: PostCreateRequest,
  ): ResponseEntity<Long> {
    return ResponseEntity.ok(postService.createPost(request.toDto()))
  }

  @PutMapping("/{id}")
  fun updatePost(
    @PathVariable("id") id: Long,
    @RequestBody request: PostUpdateRequest,
  ): ResponseEntity<Long> {
    return ResponseEntity.ok(postService.updatePost(id, request.toDto()))
  }

  @DeleteMapping("/{id}")
  fun deletePost(
    @PathVariable("id") id: Long,
    @PathVariable("id") deletedBy: String,
  ): ResponseEntity<Long> {
    return ResponseEntity.ok(postService.deletePost(id, deletedBy))
  }

  @GetMapping("/{id}")
  fun getPostDetail(
    @PathVariable("id") id: Long,
  ): PostDetailResponse {
    return PostDetailResponse(
      1L,
      "title",
      "content",
      "createdBy",
      "updatedBy",
      LocalDateTime.now(),
      LocalDateTime.now(),
    )
  }

  @GetMapping
  fun getPosts(
    pageable: Pageable,
    postSearchRequest: PostSearchRequest,
  ): Page<PostSummaryResponse> {
    return Page.empty()
  }
}
