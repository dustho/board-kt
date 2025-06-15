package com.dustho.boardkt.controller

import com.dustho.boardkt.controller.dto.request.PostCreateRequest
import com.dustho.boardkt.controller.dto.request.PostSearchRequest
import com.dustho.boardkt.controller.dto.request.PostUpdateRequest
import com.dustho.boardkt.controller.dto.request.toDto
import com.dustho.boardkt.controller.dto.response.PostDetailResponse
import com.dustho.boardkt.controller.dto.response.PostSummaryResponse
import com.dustho.boardkt.service.PostService
import com.dustho.boardkt.service.dto.response.toResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
  private val postService: PostService,
) {
  @PostMapping
  fun createPost(
    @RequestBody request: PostCreateRequest,
  ): ResponseEntity<Long> = ResponseEntity.ok(postService.createPost(request.toDto()))

  @PutMapping("/{id}")
  fun updatePost(
    @PathVariable("id") id: Long,
    @RequestBody request: PostUpdateRequest,
  ): ResponseEntity<Long> = ResponseEntity.ok(postService.updatePost(id, request.toDto()))

  @DeleteMapping("/{id}")
  fun deletePost(
    @PathVariable("id") id: Long,
    @PathVariable("id") deletedBy: String,
  ): ResponseEntity<Long> = ResponseEntity.ok(postService.deletePost(id, deletedBy))

  @GetMapping("/{id}")
  fun getPostDetail(
    @PathVariable("id") id: Long,
  ): ResponseEntity<PostDetailResponse> = ResponseEntity.ok(postService.findPost(id).toResponse())

  @GetMapping
  fun getPosts(
    pageable: Pageable,
    postSearchRequest: PostSearchRequest,
  ): ResponseEntity<Page<PostSummaryResponse>> =
    ResponseEntity.ok(postService.findPostPageBy(pageable, postSearchRequest.toDto()).toResponse())
}
