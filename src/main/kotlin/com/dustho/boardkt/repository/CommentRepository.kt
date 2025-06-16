package com.dustho.boardkt.repository

import com.dustho.boardkt.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>
