package com.dustho.boardkt.controller

import com.dustho.boardkt.controller.dto.request.PostCreateRequest
import com.dustho.boardkt.controller.dto.response.PostDetailResponse
import com.dustho.boardkt.controller.dto.request.PostSearchRequest
import com.dustho.boardkt.controller.dto.request.PostUpdateRequest
import com.dustho.boardkt.controller.dto.response.PostSummaryResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/posts")
class PostController {

  @PostMapping
  fun createPost(
    @RequestBody request: PostCreateRequest,
  ) {

  }

  @PutMapping("/{id}")
  fun updatePost(
    @PathVariable("id") id: Long,
    @RequestBody request: PostUpdateRequest,
  ) {

  }

  @DeleteMapping("/{id}")
  fun deletePost(
    @PathVariable("id") id: Long,
  ) {

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
