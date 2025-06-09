package com.dustho.boardkt

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController {
  @GetMapping("/sample")
  fun sample(
    @RequestParam name: String,
  ): String {
    return "Hello $name!"
  }
}
