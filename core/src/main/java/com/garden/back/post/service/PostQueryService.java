package com.garden.back.post.service;

import com.garden.back.post.domain.repository.PostQueryRepository;
import com.garden.back.post.domain.repository.request.*;
import com.garden.back.post.domain.repository.response.*;
import org.springframework.stereotype.Service;

@Service
public class PostQueryService {

    private final PostQueryRepository postQueryRepository;

    public PostQueryService(PostQueryRepository postQueryRepository) {
        this.postQueryRepository = postQueryRepository;
    }

    public FindPostDetailsResponse findPostById(Long id, Long loginUserId) {
        return postQueryRepository.findPostDetails(id, loginUserId);
    }

    public FindAllPostsResponse findAllPosts(FindAllPostParamRepositoryRequest request) {
        return postQueryRepository.findAllPosts(request);
    }

    public FindPostsAllCommentResponse findAllCommentsByPostId(Long id, Long loginUserId, FindAllPostCommentsParamRepositoryRequest request) {
        return postQueryRepository.findPostsAllComments(id, loginUserId, request);
    }

    public FindAllMyLikePostsResponse findAllByMyLike(Long loginUserId, FindAllMyLikePostsRepositoryRequest request) {
        return postQueryRepository.findAllByMyLike(loginUserId, request);
    }

    public FindAllMyPostsResponse findAllMyPosts(Long loginUserId, FindAllMyPostsRepositoryRequest request) {
        return postQueryRepository.findAllMyPosts(loginUserId, request);
    }

    public FindAllMyCommentPostsResponse findAllByMyComment(Long loginUserId, FindAllMyCommentPostsRepositoryRequest request) {
        return postQueryRepository.findAllByMyComment(loginUserId, request);
    }

    public FindAllPopularPostsResponse findAllPopularPosts(FindAllPopularRepositoryPostsRequest request) {
        return postQueryRepository.findAllPopularPosts(request);
    }
}
