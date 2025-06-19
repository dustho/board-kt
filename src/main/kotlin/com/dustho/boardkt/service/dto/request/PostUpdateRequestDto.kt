package com.dustho.boardkt.service.dto.request

data class PostUpdateRequestDto(
  val title: String,
  val content: String,
  val updatedBy: String,
  val tags: List<String> = emptyList(),
)
