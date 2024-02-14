package com.garden.back.post.domain.repository.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garden.back.post.domain.PostType;

import java.time.LocalDate;
import java.util.List;

public record FindAllPopularPostsResponse(
    List<PostInfo> postInfos
) {
    public record PostInfo(
        Long postId,
        String title,
        Long likeCount,
        Long commentCount,
        String content,
        String preview,
        Long authorId,
        PostType postType,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate createdDate
    ) {}
}