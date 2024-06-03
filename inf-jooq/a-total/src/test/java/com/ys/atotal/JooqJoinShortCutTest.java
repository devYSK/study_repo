package com.ys.atotal;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ys.atotal.film.FilmRepository;
import com.ys.atotal.film.FilmWithActor;

@SpringBootTest
public class JooqJoinShortCutTest {

    @Autowired
    FilmRepository filmRepository;

    @Test
    @DisplayName("implicitPathJoin_테스트")
    void implicitPathJoin_테스트() {

        List<FilmWithActor> original = filmRepository.findFilmWithActorsList2(1L, 10L);
        List<FilmWithActor> implicit = filmRepository.findFilmWithActorsListImplicitPathJoin(1L, 10L);

        Assertions.assertThat(original)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(implicit);
    }

    @Test
    @DisplayName("explicitPathJoin_테스트")
    void explicitPathJoin_테스트() {

        List<FilmWithActor> original = filmRepository.findFilmWithActorsList2(1L, 10L);
        List<FilmWithActor> implicit = filmRepository.findFilmWithActorsListExplicitPathJoin(1L, 10L);

        Assertions.assertThat(original)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(implicit);
    }

}
