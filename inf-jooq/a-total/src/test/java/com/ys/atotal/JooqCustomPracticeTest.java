package com.ys.atotal;

import org.assertj.core.api.Assertions;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ys.atotal.film.FilmRepository;
import com.ys.atotal.film.FilmService;
import com.ys.atotal.film.SimpleFilmInfo;
import com.ys.atotal.response.FilmWithActorPagedResponse;

@SpringBootTest
public class JooqCustomPracticeTest {

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    FilmService filmService;

    @Test
    @DisplayName("1) 영화정보 조회")
    void test() {
        Film film = filmRepository.findById(1L);
        Assertions.assertThat(film).isNotNull();
    }

    @Test
    @DisplayName("2) 영화정보 간략 조회")
    void test2() {
        SimpleFilmInfo simpleFilmInfo = filmService.getSimpleFilmInfo(1L);
        Assertions.assertThat(simpleFilmInfo).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("3) 영화와 배우 정보를 페이징하여 조회. - 응답까지")
    void test3() {
        FilmWithActorPagedResponse filmActorPageResponse = filmService.getFilmActorPageResponse(1L, 10L);
        Assertions.assertThat(filmActorPageResponse)
                .extracting("filmActor").asList().isNotEmpty();
    }
}
