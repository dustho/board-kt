package com.dustho.boardkt.repository

import com.dustho.boardkt.domain.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {
  fun findByNameIn(names: List<String>): List<Tag>
}
