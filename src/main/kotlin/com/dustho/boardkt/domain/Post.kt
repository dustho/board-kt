package com.dustho.boardkt.domain

import com.dustho.boardkt.exception.PostNotUpdatableException
import com.dustho.boardkt.service.dto.request.PostUpdateRequestDto
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Post(
  title: String,
  content: String,
  createdBy: String,
  postTags: List<PostTag> = emptyList(),
) : BaseEntity(createdBy) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0

  var title: String = title
    protected set

  var content: String = content
    protected set

  @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = [(CascadeType.ALL)])
  var comments: MutableList<Comment> = mutableListOf()
    protected set

  @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = [(CascadeType.ALL)])
  var postTags: MutableList<PostTag> = postTags.toMutableList()
    protected set

  fun update(dto: PostUpdateRequestDto) {
    if (dto.title.length < 2) throw PostNotUpdatableException()
    this.title = dto.title
    this.content = dto.content
    super.updateBaseProps(dto.updatedBy)
  }

  fun addTags(tags: List<Tag>) {
    tags.map { tag ->
      val isNotExist = this.postTags.any { it.tag != tag }
      if (isNotExist) {
        val newPostTag = PostTag(post = this, tag = tag)
        tag.postTags.add(newPostTag)
        this.postTags.add(newPostTag)
      }
    }
  }
}
