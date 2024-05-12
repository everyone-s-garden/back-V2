package com.garden.back.auth.client.naver;

import com.garden.back.auth.client.naver.response.NaverOauth2Response;
import com.garden.back.auth.client.naver.response.NaverTokenResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "${oauth2.naver.name}",
    url = "${oauth2.naver.url}",
    fallbackFactory = NaverOAuthClient.NaverOAuthClientFallback.class
)
public interface NaverOAuthClient {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    NaverOauth2Response getUserInfoFromNaver(@RequestHeader(name = "Authorization") String authorization);

    @Slf4j
    @Component
    class NaverOAuthClientFallback implements FallbackFactory<NaverOAuthClient> {

        @Override
        public NaverOAuthClient create(Throwable cause) {
            log.warn("Naver OAuth2 오류 {}", cause.getMessage());
            throw new IllegalArgumentException(cause.getMessage());
        }
    }

    @FeignClient(
        name = "naverToken",
        url = "https://nid.naver.com/oauth2.0/token",
        fallbackFactory = NaverOAuthClient.NaverOAuthClientFallback.class
    )
    interface NaverTokenClient {
        @GetMapping
        NaverTokenResponse getToken(@SpringQueryMap NaverTokenRequest request);
    }
}
