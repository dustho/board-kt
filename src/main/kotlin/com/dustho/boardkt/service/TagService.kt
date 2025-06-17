package com.dustho.boardkt.service

import com.dustho.boardkt.domain.Tag
import com.dustho.boardkt.repository.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TagService(
  private val tagRepository: TagRepository
) {
  @Transactional
  fun createNotExistedTags(tagNames: List<String>, createdBy: String): List<Tag> {
    val existingTags = tagRepository.findByNameIn(tagNames)
    val existingTagNames = existingTags.map { it.name }.toSet()
    val newTags = tagNames
      .filter { it !in existingTagNames }
      .map { tagName -> tagRepository.save(Tag(name = tagName, createdBy = createdBy)) }
    return existingTags + newTags
  }
}
