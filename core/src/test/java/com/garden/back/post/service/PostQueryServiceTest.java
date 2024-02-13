package com.garden.back.post.service;

import com.garden.back.global.IntegrationTestSupport;
import com.garden.back.member.Member;
import com.garden.back.member.repository.MemberRepository;
import com.garden.back.member.Role;
import com.garden.back.post.domain.Post;
import com.garden.back.post.domain.PostComment;
import com.garden.back.post.domain.PostType;
import com.garden.back.post.domain.repository.PostCommentRepository;
import com.garden.back.post.domain.repository.PostLikeRepository;
import com.garden.back.post.domain.repository.PostRepository;
import com.garden.back.post.domain.repository.request.*;
import com.garden.back.post.domain.repository.response.*;
import com.garden.back.post.service.request.CommentCreateServiceRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class PostQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    PostQueryService postQueryService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostCommentRepository postCommentRepository;

    @Autowired
    PostCommandService postCommandService;

    @Autowired
    PostLikeRepository postLikeRepository;

    @DisplayName("게시글 id로 게시글의 상세를 조회할 수 있다.(댓글 수, 좋아요 수, 작성자, 제목, 내용)")
    @Test
    void findPostById() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        String imageUrl = "http://example.com/image.jpg";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();
        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        Post savedPost = postRepository.save(post);
        postCommandService.addLikeToPost(post.getId(), savedMemberId);
        FindPostDetailsResponse response = new FindPostDetailsResponse(post.getCommentsCount(), post.getLikesCount(), nickname, content, title, savedPost.getCreatedDate(), true, List.of(imageUrl));

        //when & then
        assertThat(postQueryService.findPostById(savedPost.getId(), savedMemberId)).isEqualTo(response);
    }

    @DisplayName("모든 게시글을 댓글 순으로 정렬해서 조회할 수 있다.")
    @Test
    void findAllPostsOrderByComments() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();

        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        post.increaseLikeCount(); // 좋아요 수 많음, 더 최근 게시글
        Long savedPostId1 = postRepository.save(post).getId();

        Post post2 = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        post2.increaseCommentCount(); //댓글 수 많음, 더 늦은 게시글
        Long savedPostId2 = postRepository.save(post2).getId();

        List<FindAllPostsResponse.PostInfo> postInfosForCommentCount = List.of(
            new FindAllPostsResponse.PostInfo(savedPostId2, title, post2.getLikesCount(), post2.getCommentsCount(), post2.getContent(), post2.getPostImages().stream().findFirst().get().getImageUrl(), post2.getPostAuthorId(), post2.getPostType(), post2.getCreatedDate()),
            new FindAllPostsResponse.PostInfo(savedPostId1, title, post.getLikesCount(), post.getCommentsCount(), post.getContent(), post.getPostImages().stream().findFirst().get().getImageUrl(), post.getPostAuthorId(), post.getPostType(), post.getCreatedDate()) //Post2가 댓글 더 많음
        );

        FindAllPostParamRepositoryRequest request = new FindAllPostParamRepositoryRequest(0, 10, title, PostType.QUESTION, FindAllPostParamRepositoryRequest.OrderBy.COMMENT_COUNT);

        //when & then
        FindAllPostsResponse expectedResponseForCommentCount = new FindAllPostsResponse(postInfosForCommentCount);
        assertThat(postQueryService.findAllPosts(request)).isEqualTo(expectedResponseForCommentCount);
    }

    @DisplayName("모든 게시글을 게시글 타입으로 조회할 수 있다.")
    @Test
    void findAllPostsByPostType() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();

        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        post.increaseLikeCount(); // 좋아요 수 많음, 더 최근 게시글
        Long savedPostId1 = postRepository.save(post).getId();

        Post post2 = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.ETC);
        post2.increaseCommentCount(); //댓글 수 많음, 더 늦은 게시글
        Long savedPostId2 = postRepository.save(post2).getId();

        List<FindAllPostsResponse.PostInfo> postInfosForCommentCount = List.of(
            new FindAllPostsResponse.PostInfo(savedPostId1, title, post.getLikesCount(), post.getCommentsCount(), post.getContent(), post.getPostImages().stream().findFirst().get().getImageUrl(), post.getPostAuthorId(), post.getPostType(), post.getCreatedDate()) //Post2가 댓글 더 많음
        );

        FindAllPostParamRepositoryRequest request = new FindAllPostParamRepositoryRequest(0, 10, title, PostType.QUESTION, FindAllPostParamRepositoryRequest.OrderBy.COMMENT_COUNT);

        //when & then
        FindAllPostsResponse expectedResponseForCommentCount = new FindAllPostsResponse(postInfosForCommentCount);
        assertThat(postQueryService.findAllPosts(request)).isEqualTo(expectedResponseForCommentCount);
    }

    @DisplayName("모든 게시글을 좋아요 순으로 정렬해서 조회할 수 있다.")
    @Test
    void findAllPostsOrderByLikes() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();

        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        post.increaseLikeCount(); // 좋아요 수 많음, 더 최근 게시글
        Long savedPostId1 = postRepository.save(post).getId();

        Post post2 = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        post2.increaseCommentCount(); //댓글 수 많음, 더 늦은 게시글
        Long savedPostId2 = postRepository.save(post2).getId();

        List<FindAllPostsResponse.PostInfo> postInfosForCommentCount = List.of(
            new FindAllPostsResponse.PostInfo(savedPostId1, title, post.getLikesCount(), post.getCommentsCount(), post.getContent(), post.getPostImages().stream().findFirst().get().getImageUrl(),post.getPostAuthorId(), post.getPostType(), post.getCreatedDate()),
            new FindAllPostsResponse.PostInfo(savedPostId2, title, post2.getLikesCount(), post2.getCommentsCount(), post2.getContent(), post2.getPostImages().stream().findFirst().get().getImageUrl(), post2.getPostAuthorId(), post2.getPostType(), post2.getCreatedDate())
        );

        FindAllPostParamRepositoryRequest request = new FindAllPostParamRepositoryRequest(0, 10, title, PostType.QUESTION, FindAllPostParamRepositoryRequest.OrderBy.LIKE_COUNT);

        //when & then
        FindAllPostsResponse expectedResponseForCommentCount = new FindAllPostsResponse(postInfosForCommentCount);
        assertThat(postQueryService.findAllPosts(request)).isEqualTo(expectedResponseForCommentCount);
    }

    @DisplayName("모든 게시글을 최신 생성일 순으로 정렬해서 조회할 수 있다.")
    @Test
    void findAllPostsOrderByRecent() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();

        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        post.increaseLikeCount(); // 좋아요 수 많음, 더 최근 게시글
        Long savedPostId1 = postRepository.save(post).getId();

        Post post2 = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        post2.increaseCommentCount(); //댓글 수 많음, 더 늦은 게시글
        Long savedPostId2 = postRepository.save(post2).getId();

        List<FindAllPostsResponse.PostInfo> postInfosForCommentCount = List.of(
            new FindAllPostsResponse.PostInfo(savedPostId2, title, post2.getLikesCount(), post2.getCommentsCount(), post2.getContent(), post2.getPostImages().stream().findFirst().get().getImageUrl(), post2.getPostAuthorId(), post2.getPostType(), post2.getCreatedDate()),
            new FindAllPostsResponse.PostInfo(savedPostId1, title, post.getLikesCount(), post.getCommentsCount(), post.getContent(), post.getPostImages().stream().findFirst().get().getImageUrl(), post.getPostAuthorId(), post.getPostType(), post.getCreatedDate()) //Post2가 댓글 더 많음

        );

        FindAllPostParamRepositoryRequest request = new FindAllPostParamRepositoryRequest(0, 10, null, PostType.QUESTION, FindAllPostParamRepositoryRequest.OrderBy.RECENT_DATE);

        //when & then
        FindAllPostsResponse expectedResponseForCommentCount = new FindAllPostsResponse(postInfosForCommentCount);
        assertThat(postQueryService.findAllPosts(request)).isEqualTo(expectedResponseForCommentCount);
    }

    @DisplayName("모든 게시글을 오래된 생성일 순으로 정렬해서 조회할 수 있다.")
    @Test
    void findAllPostsOrderByOlder() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();

        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        post.increaseLikeCount(); // 좋아요 수 많음, 더 최근 게시글
        Long savedPostId1 = postRepository.save(post).getId();

        Post post2 = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        post2.increaseCommentCount(); //댓글 수 많음, 더 늦은 게시글
        Long savedPostId2 = postRepository.save(post2).getId();

        List<FindAllPostsResponse.PostInfo> postInfosForCommentCount = List.of(
            new FindAllPostsResponse.PostInfo(savedPostId1, title, post.getLikesCount(), post.getCommentsCount(), post.getContent(), post.getPostImages().stream().findFirst().get().getImageUrl(), post.getPostAuthorId(), post.getPostType(), post.getCreatedDate()),
            new FindAllPostsResponse.PostInfo(savedPostId2, title, post2.getLikesCount(), post2.getCommentsCount(), post2.getContent(), post2.getPostImages().stream().findFirst().get().getImageUrl(), post.getPostAuthorId(), post2.getPostType(), post2.getCreatedDate())
        );

        FindAllPostParamRepositoryRequest request = new FindAllPostParamRepositoryRequest(0, 10, title, PostType.QUESTION, FindAllPostParamRepositoryRequest.OrderBy.OLDER_DATE);

        //when & then
        FindAllPostsResponse expectedResponseForCommentCount = new FindAllPostsResponse(postInfosForCommentCount);
        assertThat(postQueryService.findAllPosts(request)).isEqualTo(expectedResponseForCommentCount);
    }

    @DisplayName("댓글을 최근 순으로 정렬해서 조회할 수 있다.")
    @Test
    void findAllCommentsByPostIdOrderByOlderDate() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();

        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        Long savedPostId = postRepository.save(post).getId();
        PostComment postComment = PostComment.create(null, savedMemberId, content, savedPostId);
        Long olderCommentId = postCommentRepository.save(postComment).getId();

        PostComment postComment2 = PostComment.create(null, savedMemberId, content, savedPostId);

        Long recentCommentId = postCommentRepository.save(postComment2).getId();
        postCommandService.addLikeToComment(recentCommentId, savedMemberId);

        //when & then
        FindAllPostCommentsParamRepositoryRequest request = new FindAllPostCommentsParamRepositoryRequest(0, 10, FindAllPostCommentsParamRepositoryRequest.OrderBy.RECENT_DATE);
        FindPostsAllCommentResponse response = new FindPostsAllCommentResponse(
            List.of(
                new FindPostsAllCommentResponse.CommentInfo(recentCommentId, null, postComment2.getLikesCount(), content, nickname, true),
                new FindPostsAllCommentResponse.CommentInfo(olderCommentId, null, postComment.getLikesCount(), content, nickname, false)
            )
        );
        assertThat(postQueryService.findAllCommentsByPostId(savedPostId, savedMemberId, request)).isEqualTo(response);
    }

    @DisplayName("댓글을 좋아요 순으로 정렬해서 조회할 수 있다.")
    @Test
    void findAllCommentsByPostIdOlderByRecentDate() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();

        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        Long savedPostId = postRepository.save(post).getId();
        PostComment postComment = PostComment.create(null, savedMemberId, content, savedPostId);
        Long haveLessCommentLikeId = postCommentRepository.save(postComment).getId();

        PostComment postComment2 = PostComment.create(null, savedMemberId, content, savedPostId);
        Long havMoreLikeCommentId = postCommentRepository.save(postComment2).getId(); // 좋아요 더 많음
        postCommandService.addLikeToComment(havMoreLikeCommentId, savedMemberId);

        //when & then
        FindAllPostCommentsParamRepositoryRequest request = new FindAllPostCommentsParamRepositoryRequest(0, 10, FindAllPostCommentsParamRepositoryRequest.OrderBy.LIKE_COUNT);

        FindPostsAllCommentResponse response = new FindPostsAllCommentResponse(
            List.of(
                new FindPostsAllCommentResponse.CommentInfo(havMoreLikeCommentId, null, postComment2.getLikesCount(), content, nickname, true),
                new FindPostsAllCommentResponse.CommentInfo(haveLessCommentLikeId, null, postComment.getLikesCount(), content, nickname, false)
            )
        );
        assertThat(postQueryService.findAllCommentsByPostId(savedPostId, savedMemberId, request)).isEqualTo(response);
    }

    @DisplayName("댓글을 오래된 순으로 최근 순으로 정렬해서 조회할 수 있다.")
    @Test
    void findAllCommentsByPostIdOrderByLikeCount() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();

        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        Long savedPostId = postRepository.save(post).getId();
        PostComment postComment = PostComment.create(null, savedMemberId, content, savedPostId);
        Long olderCommentId = postCommentRepository.save(postComment).getId();

        PostComment postComment2 = PostComment.create(null, savedMemberId, content, savedPostId);
        Long recentCommentId = postCommentRepository.save(postComment2).getId();
        postCommandService.addLikeToComment(recentCommentId, savedMemberId);

        //when & then
        FindAllPostCommentsParamRepositoryRequest request = new FindAllPostCommentsParamRepositoryRequest(0, 10, FindAllPostCommentsParamRepositoryRequest.OrderBy.OLDER_DATE);
        FindPostsAllCommentResponse response = new FindPostsAllCommentResponse(
            List.of(
                new FindPostsAllCommentResponse.CommentInfo(olderCommentId, null, postComment.getLikesCount(), content, nickname, false),
                new FindPostsAllCommentResponse.CommentInfo(recentCommentId, null, postComment2.getLikesCount(), content, nickname, true)
            )
        );
        assertThat(postQueryService.findAllCommentsByPostId(savedPostId, savedMemberId, request)).isEqualTo(response);
    }


    @DisplayName("내가 좋아요한 게시글을 조회한다.")
    @Test
    void findAllByMyLike() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();
        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        postRepository.save(post);
        postCommandService.addLikeToPost(post.getId(), member.getId());
        FindAllMyLikePostsResponse expected = new FindAllMyLikePostsResponse(List.of(new FindAllMyLikePostsResponse.PostInfo(post.getId(), post.getTitle())));

        //when & then
        FindAllMyLikePostsResponse actual = postQueryService.findAllByMyLike(member.getId(), new FindAllMyLikePostsRepositoryRequest(0L, 10L));
        assertThat(expected).isEqualTo(actual);

    }

    @DisplayName("내가 작성한 게시글을 조회한다.")
    @Test
    void findAllMyPosts() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();
        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        postRepository.save(post);
        FindAllMyPostsResponse expected = new FindAllMyPostsResponse(List.of(new FindAllMyPostsResponse.PostInfo(post.getId(), post.getTitle())));

        //when & then
        FindAllMyPostsResponse actual = postQueryService.findAllMyPosts(member.getId(), new FindAllMyPostsRepositoryRequest(0L, 10L));
        assertThat(expected).isEqualTo(actual);


    }

    @DisplayName("내가 작성한 댓글의 게시글을 조회한다.")
    @Test
    void findAllByMyComment() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();
        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        postRepository.save(post);
        postCommandService.createComment(post.getId(), member.getId(), new CommentCreateServiceRequest("내용", null));
        FindAllMyCommentPostsResponse expected = new FindAllMyCommentPostsResponse(List.of(new FindAllMyCommentPostsResponse.PostInfo(post.getId(), post.getTitle())));

        //when & then
        FindAllMyCommentPostsResponse actual = postQueryService.findAllByMyComment(member.getId(), new FindAllMyCommentPostsRepositoryRequest(0L, 10L));
        assertThat(expected).isEqualTo(actual);
    }

    @DisplayName("실시간 인기 게시글을 조회한다.")
    @Test
    void findPopularPosts() {
        //given
        String content = "내용";
        String nickname = "닉네임";
        String title = "제목";
        Member member = Member.create("asdf@example.com", nickname, Role.USER);
        Long savedMemberId = memberRepository.save(member).getId();

        Post post = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        post.increaseLikeCount(); // 좋아요 수 많음
        Long savedPostId1 = postRepository.save(post).getId();

        Post post2 = Post.create(title, content, savedMemberId, List.of("http://example.com/image.jpg"), PostType.QUESTION);
        post2.increaseCommentCount(); //댓글 수 많음
        Long savedPostId2 = postRepository.save(post2).getId();

        //when & then
        FindAllPopularRepositoryPostsRequest request = new FindAllPopularRepositoryPostsRequest(0L, 10L, 1);
        FindAllPopularPostsResponse response = new FindAllPopularPostsResponse(List.of(
            new FindAllPopularPostsResponse.PostInfo(savedPostId1, title, post.getLikesCount(), post.getCommentsCount(), post.getCreatedDate()),
            new FindAllPopularPostsResponse.PostInfo(savedPostId2, title, post2.getLikesCount(), post2.getCommentsCount(), post2.getCreatedDate())
        ));
        assertThat(postQueryService.findAllPopularPosts(request)).isEqualTo(response);
    }
}