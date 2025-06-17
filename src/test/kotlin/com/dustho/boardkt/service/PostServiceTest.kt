package com.dustho.boardkt.service

import com.dustho.boardkt.domain.Comment
import com.dustho.boardkt.domain.Post
import com.dustho.boardkt.exception.PostNotDeletableException
import com.dustho.boardkt.exception.PostNotFoundException
import com.dustho.boardkt.exception.PostNotUpdatableException
import com.dustho.boardkt.repository.CommentRepository
import com.dustho.boardkt.repository.PostRepository
import com.dustho.boardkt.service.dto.request.PostCreateRequestDto
import com.dustho.boardkt.service.dto.request.PostSearchRequestDto
import com.dustho.boardkt.service.dto.request.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
  private val postService: PostService,
  private val postRepository: PostRepository,
  private val commentRepository: CommentRepository,
) : BehaviorSpec({
    given("게시글 생성 시,") {
      When("입력 값이 정상적으로 들어오면") {
        val postId =
          postService.createPost(
            PostCreateRequestDto(
              title = "제목",
              content = "내용",
              createdBy = "harris",
            ),
          )
        then("게시글이 정상적으로 생성됩니다.") {
          postId shouldBeGreaterThan 0L
          val post = postRepository.findByIdOrNull(postId)
          post shouldNotBe null
          post?.title shouldBe "제목"
          post?.content shouldBe "내용"
          post?.createdBy shouldBe "harris"
        }
      }
    }

    given("게시글 수정 시,") {
      val savedPost = postRepository.save(Post(title = "제목", content = "내용", createdBy = "harris"))
      When("입력 값이 정상적으로 들어오면") {
        val postId =
          postService.updatePost(
            savedPost.id,
            PostUpdateRequestDto(
              title = "제목",
              content = "수정된 내용",
              updatedBy = "tom",
            ),
          )
        then("게시글이 정상적으로 수정됩니다.") {
          postId shouldBe savedPost.id
          val post = postRepository.findByIdOrNull(postId)
          post shouldNotBe null
          post?.title shouldBe "제목"
          post?.content shouldBe "수정된 내용"
          post?.updatedBy shouldBe "tom"
        }
      }
      When("동일한 게시글을 찾을 수 없으면") {
        then("'게시글을 찾을 수 없다'라는 예외가 발생합니다.") {
          shouldThrow<PostNotFoundException> {
            postService.updatePost(
              -1,
              PostUpdateRequestDto(
                title = "제목",
                content = "수정된 내용",
                updatedBy = "tom",
              ),
            )
          }
        }
      }
      When("제목이 2글자 미만이라면") {
        then("'게시글을 수정할 수 없다'라는 예외가 발생합니다.") {
          shouldThrow<PostNotUpdatableException> {
            postService.updatePost(
              savedPost.id,
              PostUpdateRequestDto(
                title = "제",
                content = "수정된 내용",
                updatedBy = "tom",
              ),
            )
          }
        }
      }
    }

    given("게시글 삭제 시,") {
      val savedPost = postRepository.save(Post(title = "제목", content = "내용", createdBy = "harris"))
      When("입력 값이 정상적으로 들어오면") {
        val postId = postService.deletePost(savedPost.id, savedPost.createdBy)
        then("게시글이 정상적으로 삭제됩니다.") {
          postId shouldBe savedPost.id
          postRepository.findByIdOrNull(postId) shouldBe null
        }
      }
      When("요청자와 작성자가 동일하지 않으면") {
        val savedPost2 = postRepository.save(Post(title = "제목", content = "내용", createdBy = "harris"))
        then("'게시글을 삭제할 수 없다'라는 예외가 발생합니다.") {
          shouldThrow<PostNotDeletableException> { postService.deletePost(savedPost2.id, "${savedPost2.createdBy}123") }
        }
      }
    }

    given("게시글 조회 시,") {
      val savedPost = postRepository.save(Post(title = "제목", content = "내용", createdBy = "harris"))
      When("입력 값이 정상적으로 들어오면") {
        val selectedPost = postService.findPost(savedPost.id)
        then("게시글이 정상적으로 조회됩니다.") {
          selectedPost.id shouldBe savedPost.id
          selectedPost.title shouldBe savedPost.title
          selectedPost.content shouldBe savedPost.content
          selectedPost.createdBy shouldBe savedPost.createdBy
          selectedPost.createdAt shouldBe savedPost.createdAt
          selectedPost.updatedBy shouldBe savedPost.updatedBy
          selectedPost.updatedAt shouldBe savedPost.updatedAt
        }
      }
      When("요청된 id에 해당하는 게시글이 없다면") {
        then("'게시글을 조회할 수 없다.'라는 예외가 발생합니다.") {
          shouldThrow<PostNotFoundException> { postService.findPost(0) }
        }
      }
      When("댓글이 있는 게시글이라면") {
        commentRepository.save(Comment("댓글 내용1", savedPost, "dustho"))
        commentRepository.save(Comment("댓글 내용2", savedPost, "dustho2"))
        val selectedPost = postService.findPost(savedPost.id)
        then("'게시글을 조회할 수 없다.'라는 예외가 발생합니다.") {
          selectedPost.comments.size shouldBe 2
          selectedPost.comments[0].content shouldBe "댓글 내용1"
          selectedPost.comments[1].content shouldBe "댓글 내용2"
        }
      }
    }

    given("게시글 목록 조회 시,") {
      postRepository.saveAll(
        listOf(
          Post(title = "제목1", content = "내용", createdBy = "harris"),
          Post(title = "제목2", content = "내용", createdBy = "harris"),
          Post(title = "제목3", content = "내용", createdBy = "harris"),
          Post(title = "제목4", content = "내용", createdBy = "harris"),
          Post(title = "제목5", content = "내용", createdBy = "harris"),
          Post(title = "제목6", content = "내용", createdBy = "harris"),
          Post(title = "제목7", content = "내용", createdBy = "harris"),
          Post(title = "제목8", content = "내용", createdBy = "harris"),
          Post(title = "제목9", content = "내용", createdBy = "harris"),
          Post(title = "제목10", content = "내용", createdBy = "harris"),
        ),
      )
      When("입력 값이 정상적으로 들어오면") {
        val selectedPostPage = postService.findPostPageBy(PageRequest.of(0, 5), PostSearchRequestDto())
        then("게시글이 정상적으로 조회됩니다.") {
          selectedPostPage.number shouldBe 0
          selectedPostPage.size shouldBe 5
          selectedPostPage.content.size shouldBe 5
        }
      }
      When("일치하는 제목이 있으면") {
        val selectedPostPage = postService.findPostPageBy(PageRequest.of(0, 5), PostSearchRequestDto(title = "제목1"))
        then("게시글이 정상적으로 조회됩니다.") {
          selectedPostPage.number shouldBe 0
          selectedPostPage.size shouldBe 5
          selectedPostPage.content.size shouldBe 2
        }
      }
      When("일치하는 내용이 있으면") {
        val selectedPostPage = postService.findPostPageBy(PageRequest.of(0, 5), PostSearchRequestDto(content = "내용"))
        then("게시글이 정상적으로 조회됩니다.") {
          selectedPostPage.number shouldBe 0
          selectedPostPage.size shouldBe 5
          selectedPostPage.content.size shouldBe 5
        }
      }
    }
  })
