package com.dustho.boardkt.service

import com.dustho.boardkt.domain.Post
import com.dustho.boardkt.exception.PostNotDeletableException
import com.dustho.boardkt.exception.PostNotFoundException
import com.dustho.boardkt.exception.PostNotUpdatableException
import com.dustho.boardkt.repository.PostRepository
import com.dustho.boardkt.service.dto.PostCreateRequestDto
import com.dustho.boardkt.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
  private val postService: PostService,
  private val postRepository: PostRepository,
) : BehaviorSpec({
  given("게시글 생성 시,") {
    When("입력 값이 정상적으로 들어오면") {
      val postId = postService.createPost(PostCreateRequestDto(
        title = "제목",
        content = "내용",
        createdBy = "harris",
      ))
      then("게시글이 정상적으로 생성됩니다.") {
        postId shouldBeGreaterThan 0L
        val post = postRepository.findByIdOrNull(postId)
        post shouldNotBe  null
        post?.title shouldBe  "제목"
        post?.content shouldBe "내용"
        post?.createdBy shouldBe "harris"
      }
    }
  }

  given("게시글 수정 시,") {
    val savedPost = postRepository.save(Post(title = "제목", content = "내용", createdBy = "harris",))
    When("입력 값이 정상적으로 들어오면") {
      val postId = postService.updatePost(savedPost.id, PostUpdateRequestDto(
        title = "제목",
        content = "수정된 내용",
        updatedBy = "tom",
      ))
      then("게시글이 정상적으로 수정됩니다.") {
        postId shouldBe savedPost.id
        val post = postRepository.findByIdOrNull(postId)
        post shouldNotBe  null
        post?.title shouldBe  "제목"
        post?.content shouldBe "수정된 내용"
        post?.updatedBy shouldBe "tom"
      }
    }
    When("동일한 게시글을 찾을 수 없으면") {
      then("'게시글을 찾을 수 없다'라는 예외가 발생합니다.") {
        shouldThrow<PostNotFoundException> {
          postService.updatePost(-1, PostUpdateRequestDto(
            title = "제목",
            content = "수정된 내용",
            updatedBy = "tom",
          ))
        }
      }
    }
    When("제목이 2글자 미만이라면") {
      then("'게시글을 수정할 수 없다'라는 예외가 발생합니다.") {
        shouldThrow<PostNotUpdatableException> {
          postService.updatePost(savedPost.id, PostUpdateRequestDto(
            title = "제",
            content = "수정된 내용",
            updatedBy = "tom",
          ))
        }
      }
    }
  }

  given("게시글 삭제 시,") {
    val savedPost = postRepository.save(Post(title = "제목", content = "내용", createdBy = "harris",))
    When("입력 값이 정상적으로 들어오면") {
      val postId = postService.deletePost(savedPost.id, savedPost.createdBy)
      then("게시글이 정상적으로 삭제됩니다.") {
        postId shouldBe savedPost.id
        postRepository.findByIdOrNull(postId) shouldBe null
      }
    }
    When("요청자와 작성자가 동일하지 않으면") {
      val savedPost2 = postRepository.save(Post(title = "제목", content = "내용", createdBy = "harris",))
      then("'게시글을 삭제할 수 없다'라는 예외가 발생합니다.") {
        shouldThrow<PostNotDeletableException> { postService.deletePost(savedPost2.id, "${savedPost2.createdBy}123") }
      }
    }
  }
})
