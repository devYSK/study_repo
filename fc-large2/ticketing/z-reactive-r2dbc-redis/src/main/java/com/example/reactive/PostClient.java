package com.example.reactive;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.reactive.domain.post.dto.PostResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostClient {
    private final WebClient webClient;
    private final String url = "http://127.0.0.1:8090";

    public Mono<PostResponse> getPost(Long id) {
        String uriString = UriComponentsBuilder.fromHttpUrl(url)
                .path("/posts/%d".formatted(id))
                .buildAndExpand()
                .toUriString();

        return webClient.get()
                .uri(uriString)
                .retrieve()
                .bodyToMono(PostResponse.class);
    }
}
