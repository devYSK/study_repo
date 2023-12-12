package com.fastcampus.projectboard.repository.querydsl;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.fastcampus.projectboard.domain.QHashtag;
import com.querydsl.jpa.JPQLQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

	public ArticleRepositoryCustomImpl() {
		super(Article.class);
	}

	private static final QArticle article = QArticle.article;
	private static final QHashtag hashtag = QHashtag.hashtag;

	@Override
	public List<String> findAllDistinctHashtags() {

		return from(article)
			.distinct()
			.select(article.hashtags.any().hashtagName)
			.fetch();
	}

	@Override
	public Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable) {
		JPQLQuery<Article> query = from(article)
			.innerJoin(article.hashtags, hashtag)
			.where(hashtag.hashtagName.in(hashtagNames));

		List<Article> articles = Objects.requireNonNull(getQuerydsl())
										.applyPagination(pageable, query)
										.fetch();

		return new PageImpl<>(articles, pageable, query.fetchCount());
	}

}
