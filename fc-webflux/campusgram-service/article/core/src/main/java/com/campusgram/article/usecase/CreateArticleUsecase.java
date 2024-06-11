package com.campusgram.article.usecase;

import java.util.List;
import java.util.stream.Collectors;

import com.campusgram.article.entity.Article;
import com.campusgram.article.entity.ArticleThumbnail;
import com.campusgram.article.entity.ArticleThumbnailIdOnly;
import com.campusgram.article.publisher.ArticleCreatedEvent;
import com.campusgram.article.publisher.ArticleEventPublisher;
import com.campusgram.article.repository.ArticleRepository;

import jakarta.inject.Named;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Named
public class CreateArticleUsecase {
    private final ArticleRepository articleRepository;
    private final ArticleEventPublisher articleEventPublisher;

    @Data
    @EqualsAndHashCode
    public static class Input {
        private final String title;
        private final String content;
        private final List<String> thumbnailImageIds;
        private final String creatorId;
    }

    public Mono<Article> execute(Input input) {
        List<ArticleThumbnail> thumbnails = input.thumbnailImageIds.stream()
                .map(ArticleThumbnailIdOnly::new)
                .collect(Collectors.toList());

        var newArticle = Article.create(
                input.title,
                input.content,
                thumbnails,
                input.creatorId
        );

        return articleRepository.save(newArticle)
                .doOnNext(article -> {
                    var event = new ArticleCreatedEvent(
                            article.getId(), article.getCreatorId()
                    );
                    articleEventPublisher.publish(event);
                });
    }
}
