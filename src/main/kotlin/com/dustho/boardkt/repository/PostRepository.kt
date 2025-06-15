package com.dustho.boardkt.repository

import com.dustho.boardkt.domain.Post
import com.dustho.boardkt.domain.QPost.post
import com.dustho.boardkt.service.dto.request.PostSearchRequestDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

interface PostRepository :
  JpaRepository<Post, Long>,
  CustomPostRepository

interface CustomPostRepository {
  fun findPageBy(
    pageable: Pageable,
    postSearchRequest: PostSearchRequestDto,
  ): Page<Post>
}

class CustomPostRepositoryImpl :
  QuerydslRepositorySupport(Post::class.java),
  CustomPostRepository {
  override fun findPageBy(
    pageable: Pageable,
    postSearchRequest: PostSearchRequestDto,
  ): Page<Post> {
    val result =
      from(post)
        .where(
          postSearchRequest.title?.let { post.title.contains(postSearchRequest.title) },
          postSearchRequest.content?.let { post.content.contains(postSearchRequest.content) },
        ).orderBy(post.createdAt.desc())
        .offset(pageable.offset)
        .limit(pageable.pageSize.toLong())
        .fetchResults()

    return PageImpl(result.results, pageable, result.total)
  }
}
