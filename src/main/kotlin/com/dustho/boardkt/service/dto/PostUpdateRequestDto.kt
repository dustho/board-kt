package com.dustho.boardkt.service.dto

data class PostUpdateRequestDto(
  val title: String,
  val content: String,
  val updatedBy: String,
)
