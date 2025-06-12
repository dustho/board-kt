package com.dustho.boardkt.repository

import com.dustho.boardkt.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
}
