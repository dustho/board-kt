package com.dustho.boardkt.domain

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class PostTag(
  post: Post,
  tag: Tag,
) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  val post: Post = post

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_id")
  val tag: Tag = tag
}
