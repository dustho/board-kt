package com.dustho.boardkt.repository

import com.dustho.boardkt.domain.PostTag
import org.springframework.data.jpa.repository.JpaRepository

interface PostTagRepository : JpaRepository<PostTag, Long> {}
