package com.dustho.boardkt.service

import com.dustho.boardkt.domain.Comment
import com.dustho.boardkt.domain.Post
import com.dustho.boardkt.exception.CommentNotDeletableException
import com.dustho.boardkt.exception.CommentNotFoundException
import com.dustho.boardkt.exception.CommentNotUpdatableException
import com.dustho.boardkt.exception.PostNotFoundException
import com.dustho.boardkt.repository.CommentRepository
import com.dustho.boardkt.repository.PostRepository
import com.dustho.boardkt.service.dto.request.CommentCreateRequestDto
import com.dustho.boardkt.service.dto.request.CommentUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class CommentServiceTest(
  private val commentService: CommentService,
  private val commentRepository: CommentRepository,
  private val postRepository: PostRepository,
) : BehaviorSpec({
  given("댓글 생성 시,") {
    postRepository.save(Post(title = "제목", content = "내용", createdBy = "harris"))
    When("입력 값이 정상적으로 들어오면") {
      val commentId = commentService.createComment(postId = 1, CommentCreateRequestDto(
        content = "댓글 내용",
        createdBy = "dustho"
      ))
      then("댓글이 정상적으로 생성됩니다.") {
        commentId shouldBeGreaterThan 0
        val comment = commentRepository.findByIdOrNull(commentId)
        comment shouldNotBe null
        comment?.id shouldBe commentId
        comment?.content shouldBe "댓글 내용"
        comment?.createdBy shouldBe "dustho"
      }
    }
    When("게시글이 존재하지 않으면") {
      then("'게시글을 찾을 수 없다'라는 예외가 발생합니다.") {
        shouldThrow<PostNotFoundException> {
          commentService.createComment(postId = 0, CommentCreateRequestDto(
            content = "댓글 내용",
            createdBy = "dustho"
          ))
        }
      }
    }
  }

  given("댓글 수정 시,") {
    val savedPost = postRepository.save(Post(title = "제목", content = "내용", createdBy = "harris"))
    val savedComment = commentRepository.save(Comment(content = "댓글 내용", post = savedPost, createdBy = "dustho"))
    When("입력 값이 정상적으로 들어오면") {
      val commentId = commentService.updateComment(commentId = savedComment.id, CommentUpdateRequestDto(
        content = "수정된 댓글 내용",
        updatedBy = "dustho"
      ))
      then("댓글이 정상적으로 수정됩니다.") {
        commentId shouldBe savedComment.id
        val comment = commentRepository.findByIdOrNull(commentId)
        comment shouldNotBe null
        comment?.id shouldBe commentId
        comment?.content shouldBe "수정된 댓글 내용"
        comment?.updatedBy shouldBe "dustho"
      }
    }
    When("댓글이 존재하지 않으면") {
      then("'댓글을 찾을 수 없다'라는 예외가 발생합니다.") {
        shouldThrow<CommentNotFoundException> {
          commentService.updateComment(commentId = 0, CommentUpdateRequestDto(
            content = "수정된 댓글 내용",
            updatedBy = "dustho"
          ))
        }
      }
    }
    When("작성자와 수정자가 동일하지 않으면") {
      then("'댓글을 수정할 수 없다'라는 예외가 발생합니다.") {
        shouldThrow<CommentNotUpdatableException> {
          commentService.updateComment(commentId = 1, CommentUpdateRequestDto(
            content = "수정된 댓글 내용",
            updatedBy = "not dustho"
          ))
        }
      }
    }
  }

  given("댓글 삭제 시,") {
    val savedPost = postRepository.save(Post(title = "제목", content = "내용", createdBy = "harris"))
    val savedComment1 = commentRepository.save(Comment(content = "댓글 내용1", post = savedPost, createdBy = "dustho"))
    val savedComment2 = commentRepository.save(Comment(content = "댓글 내용2", post = savedPost, createdBy = "dustho"))
    When("입력 값이 정상적으로 들어오면") {
      commentService.deleteComment(commentId = savedComment1.id, deletedBy = "dustho")
      then("댓글이 정상적으로 삭제됩니다.") {
        commentRepository.findByIdOrNull(savedComment1.id) shouldBe null
      }
    }
    When("댓글이 존재하지 않으면") {
      val deletedCommentId = savedComment1.id
      then("'댓글을 찾을 수 없다'라는 예외가 발생합니다.") {
        shouldThrow<CommentNotFoundException> {
          commentService.deleteComment(commentId = deletedCommentId, deletedBy = "dustho")
        }
      }
    }
    When("작성자와 삭제자가 동일하지 않으면") {
      then("'댓글을 삭제할 수 없다'라는 예외가 발생합니다.") {
        shouldThrow<CommentNotDeletableException> {
          commentService.deleteComment(commentId = savedComment2.id, deletedBy = "not dustho")
        }
      }
    }
  }
})
