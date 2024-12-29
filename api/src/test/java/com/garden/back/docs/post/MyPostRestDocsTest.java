package com.garden.back.docs.post;

import com.garden.back.docs.RestDocsSupport;
import com.garden.back.member.MemberMannerGrade;
import com.garden.back.post.MyPostController;
import com.garden.back.post.domain.PostType;
import com.garden.back.post.domain.repository.response.FindAllMyCommentPostsResponse;
import com.garden.back.post.domain.repository.response.FindAllMyLikePostsResponse;
import com.garden.back.post.domain.repository.response.FindAllMyPostsResponse;
import com.garden.back.post.domain.repository.response.UserResponse;
import com.garden.back.post.request.FindAllMyCommentPostsRequest;
import com.garden.back.post.request.FindAllMyLikePostsRequest;
import com.garden.back.post.request.FindAllMyPostsRequest;
import com.garden.back.post.service.PostQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MyPostRestDocsTest extends RestDocsSupport {
    PostQueryService postQueryService = mock(PostQueryService.class);

    @Override
    protected Object initController() {
        return new MyPostController(postQueryService);
    }

    @DisplayName("내가 작성한 댓글의 게시글 api docs")
    @Test
    void findAllByComment() throws Exception {
        FindAllMyCommentPostsResponse response = new FindAllMyCommentPostsResponse(
            List.of(
                new FindAllMyCommentPostsResponse.PostInfo(
                    1L,
                    "제목",
                    "이미지 url",
                    "댓글 내용",
                    0L,
                    0L,
                    PostType.QUESTION,
                    new UserResponse(
                        1L,
                        "이름",
                        "닉네임",
                        MemberMannerGrade.SEED,
                        "abc@email.com"
                    )
                )
            )
        );
        FindAllMyCommentPostsRequest request = new FindAllMyCommentPostsRequest(0L, 10L);
        given(postQueryService.findAllByMyComment(any(), any())).willReturn(response);

        mockMvc.perform(get("/v1/my/posts/comments")
                .param("offset", String.valueOf(request.offset()))
                .param("limit", String.valueOf(request.limit()))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-my-comment-posts",
                queryParameters(
                    parameterWithName("offset").description("조회를 시작할 데이터의 위치"),
                    parameterWithName("limit").description("해당 페이지에서 조회할 데이터의 개수")
                ),
                responseFields(
                    fieldWithPath("postInfos").type(ARRAY).description("게시글 정보 목록"),
                    fieldWithPath("postInfos[].postId").type(NUMBER).description("게시글 ID"),
                    fieldWithPath("postInfos[].title").type(STRING).description("게시글 제목"),
                    fieldWithPath("postInfos[].preview").type(STRING).description("게시글 미리보기 이미지"),
                    fieldWithPath("postInfos[].content").type(STRING).description("게시글 내용"),
                    fieldWithPath("postInfos[].likesCount").type(NUMBER).description("좋아요 수"),
                    fieldWithPath("postInfos[].commentsCount").type(NUMBER).description("댓글 수"),
                    fieldWithPath("postInfos[].postType").type(STRING).description("글 주제"),
                    fieldWithPath("postInfos[].userInfo").type(OBJECT).description("작성자 정보"),
                    fieldWithPath("postInfos[].userInfo.userId").type(NUMBER).description("작성자 ID"),
                    fieldWithPath("postInfos[].userInfo.name").type(STRING).description("작성자 닉네임"),
                    fieldWithPath("postInfos[].userInfo.profile").type(STRING).description("작성자 프로필 이미지"),
                    fieldWithPath("postInfos[].userInfo.memberMannerGrade").type(STRING).description("작성자 매너 점수 등급"),
                    fieldWithPath("postInfos[].userInfo.email").type(STRING).description("작성자 email")
                )
            ));
    }

    @DisplayName("내가 작성한 게시글 api docs")
    @Test
    void findAllMyPosts() throws Exception {
        FindAllMyPostsResponse response = new FindAllMyPostsResponse(
            List.of(
                new FindAllMyPostsResponse.PostInfo(
                    1L,
                    "제목",
                    "이미지 url",
                    "내용",
                    0L,
                    0L,
                    PostType.QUESTION,
                    new UserResponse(
                        1L,
                        "이름",
                        "닉네임",
                        MemberMannerGrade.SEED,
                        "abc@emailc.om"
                    )
                )
            )
        );
        FindAllMyPostsRequest request = new FindAllMyPostsRequest(0L, 10L);
        given(postQueryService.findAllMyPosts(any(), any())).willReturn(response);

        mockMvc.perform(get("/v1/my/posts")
                .param("offset", String.valueOf(request.offset()))
                .param("limit", String.valueOf(request.limit()))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-my-posts",
                queryParameters(
                    parameterWithName("offset").description("조회를 시작할 데이터의 위치"),
                    parameterWithName("limit").description("해당 페이지에서 조회할 데이터의 개수")
                ),
                responseFields(
                    fieldWithPath("postInfos").type(ARRAY).description("게시글 정보 목록"),
                    fieldWithPath("postInfos[].postId").type(NUMBER).description("게시글 ID"),
                    fieldWithPath("postInfos[].title").type(STRING).description("게시글 제목"),
                    fieldWithPath("postInfos[].preview").type(STRING).description("미리보기 이미지"),
                    fieldWithPath("postInfos[].content").type(STRING).description("게시글 내용"),
                    fieldWithPath("postInfos[].likesCount").type(NUMBER).description("좋아요 수"),
                    fieldWithPath("postInfos[].commentsCount").type(NUMBER).description("댓글 수"),
                    fieldWithPath("postInfos[].postType").type(STRING).description("글 주제"),
                    fieldWithPath("postInfos[].userInfo").type(OBJECT).description("작성자 정보"),
                    fieldWithPath("postInfos[].userInfo.userId").type(NUMBER).description("작성자 ID"),
                    fieldWithPath("postInfos[].userInfo.name").type(STRING).description("작성자 닉네임"),
                    fieldWithPath("postInfos[].userInfo.profile").type(STRING).description("작성자 프로필 이미지"),
                    fieldWithPath("postInfos[].userInfo.memberMannerGrade").type(STRING).description("작성자 매너 점수 등급"),
                    fieldWithPath("postInfos[].userInfo.email").type(STRING).description("작성자 email")
                )
            ));

    }

    @DisplayName("내가 좋아요한 게시글 api docs")
    @Test
    void findAllByLike() throws Exception {
        FindAllMyLikePostsResponse response = new FindAllMyLikePostsResponse(
            List.of(
                new FindAllMyLikePostsResponse.PostInfo(
                    1L,
                    "제목",
                    "미리보기 이미지",
                    "내용",
                    0L,
                    0L,
                    PostType.QUESTION,
                    new UserResponse(
                        1L,
                        "이름",
                        "닉네임",
                        MemberMannerGrade.SEED,
                        "abc@eamil.com"
                    )
                )
            )
        );
        FindAllMyLikePostsRequest request = new FindAllMyLikePostsRequest(0L, 10L);
        given(postQueryService.findAllByMyLike(any(), any())).willReturn(response);

        mockMvc.perform(get("/v1/my/posts/likes")
                .param("offset", String.valueOf(request.offset()))
                .param("limit", String.valueOf(request.limit()))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-my-likes-posts",
                queryParameters(
                    parameterWithName("offset").description("조회를 시작할 데이터의 위치"),
                    parameterWithName("limit").description("해당 페이지에서 조회할 데이터의 개수")
                ),
                responseFields(
                    fieldWithPath("postInfos").type(ARRAY).description("게시글 정보 목록"),
                    fieldWithPath("postInfos[].postId").type(NUMBER).description("게시글 ID"),
                    fieldWithPath("postInfos[].title").type(STRING).description("게시글 제목"),
                    fieldWithPath("postInfos[].preview").type(STRING).description("미리보기 이미지"),
                    fieldWithPath("postInfos[].content").type(STRING).description("게시글 내용"),
                    fieldWithPath("postInfos[].likesCount").type(NUMBER).description("좋아요 수"),
                    fieldWithPath("postInfos[].commentsCount").type(NUMBER).description("댓글 수"),
                    fieldWithPath("postInfos[].postType").type(STRING).description("글 주제"),
                    fieldWithPath("postInfos[].userInfo").type(OBJECT).description("작성자 정보"),
                    fieldWithPath("postInfos[].userInfo.userId").type(NUMBER).description("작성자 ID"),
                    fieldWithPath("postInfos[].userInfo.name").type(STRING).description("작성자 닉네임"),
                    fieldWithPath("postInfos[].userInfo.profile").type(STRING).description("작성자 프로필 이미지"),
                    fieldWithPath("postInfos[].userInfo.memberMannerGrade").type(STRING).description("작성자 매너 점수 등급"),
                    fieldWithPath("postInfos[].userInfo.email").type(STRING).description("작성자 email")
                )
            ));
    }
}
