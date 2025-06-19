package com.dustho.boardkt.service

import com.dustho.boardkt.domain.Comment
import com.dustho.boardkt.domain.Post
import com.dustho.boardkt.domain.Tag
import com.dustho.boardkt.exception.PostNotDeletableException
import com.dustho.boardkt.exception.PostNotFoundException
import com.dustho.boardkt.exception.PostNotUpdatableException
import com.dustho.boardkt.repository.CommentRepository
import com.dustho.boardkt.repository.PostRepository
import com.dustho.boardkt.repository.PostTagRepository
import com.dustho.boardkt.repository.TagRepository
import com.dustho.boardkt.service.dto.request.PostCreateRequestDto
import com.dustho.boardkt.service.dto.request.PostSearchRequestDto
import com.dustho.boardkt.service.dto.request.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class PostServiceTest(
  private val postService: PostService,
  private val postRepository: PostRepository,
  private val commentRepository: CommentRepository,
  private val tagRepository: TagRepository,
  private val postTagRepository: PostTagRepository,
) : BehaviorSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Test))

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
          val post = postRepository.findByIdOrNull(postId)!!
          post.title shouldBe "제목"
          post.content shouldBe "내용"
          post.createdBy shouldBe "harris"
        }
      }
      When("태그가 함께 들어오면") {
        val postId =
          postService.createPost(
            PostCreateRequestDto(
              title = "제목",
              content = "내용",
              createdBy = "harris",
              tags = listOf("태그1", "태그2"),
            ),
          )
        then("게시글, 태그가 함께 정상적으로 생성됩니다.") {
          val post = postRepository.findByIdOrNull(postId)!!
          post.title shouldBe "제목"
          post.content shouldBe "내용"
          post.createdBy shouldBe "harris"

          val postTags = postTagRepository.findAll()
          val tags = tagRepository.findAll()
          println(postTags.size)
          println(tags.size)

          val tag1 = tagRepository.findByIdOrNull(post.postTags[0].tag.id)!!
          val tag2 = tagRepository.findByIdOrNull(post.postTags[1].tag.id)!!
          tag1.name shouldBe "태그1"
          tag2.name shouldBe "태그2"

          val postTag1 = postTagRepository.findByIdOrNull(post.postTags[0].id)!!
          val postTag2 = postTagRepository.findByIdOrNull(post.postTags[1].id)!!
          postTag1.post shouldBeEqual post
          postTag1.tag shouldBeEqual tag1
          postTag2.post shouldBeEqual post
          postTag2.tag shouldBeEqual tag2
        }
      }
    }

    given("게시글 수정 시,") {
      val tag1 = Tag(name = "태그1", createdBy = "harris")
      val tag2 = Tag(name = "태그2", createdBy = "태그")
      val savedTag1 = tagRepository.save(tag1)
      val savedTag2 = tagRepository.save(tag2)
      val post = Post(title = "제목", content = "내용", createdBy = "harris")
      post.updateTags(listOf(savedTag1, savedTag2))
      val savedPost = postRepository.save(post)

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
      When("태그가 함께 들어오면") {
        val postId =
          postService.updatePost(
            savedPost.id,
            PostUpdateRequestDto(
              title = "제목",
              content = "내용",
              updatedBy = "harris",
              tags = listOf("태그2", "태그3"),
            ),
          )
        then("게시글, 태그가 함께 정상적으로 생성됩니다.") {
          val post = postRepository.findByIdOrNull(postId)!!
          post.title shouldBe "제목"
          post.content shouldBe "내용"
          post.createdBy shouldBe "harris"

          val tag1 = tagRepository.findByIdOrNull(post.postTags[0].tag.id)!!
          val tag2 = tagRepository.findByIdOrNull(post.postTags[1].tag.id)!!
          tag1.name shouldBe "태그1"
          tag2.name shouldBe "태그2"

          val postTag1 = postTagRepository.findByIdOrNull(post.postTags[0].id)!!
          val postTag2 = postTagRepository.findByIdOrNull(post.postTags[1].id)!!
          postTag1.post shouldBeEqual post
          postTag1.tag shouldBeEqual tag1
          postTag2.post shouldBeEqual post
          postTag2.tag shouldBeEqual tag2
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
