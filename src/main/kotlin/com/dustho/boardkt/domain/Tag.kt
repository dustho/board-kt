package com.dustho.boardkt.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Tag(
  name: String,
  postTags: List<PostTag> = emptyList(),
  createdBy: String,
) : BaseEntity(createdBy) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0

  var name: String = name
    protected set

  @OneToMany(mappedBy = "tag", orphanRemoval = true, cascade = [CascadeType.ALL])
  var postTags: MutableList<PostTag> = postTags.toMutableList()
    protected set
}
