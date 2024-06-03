package com.ys.atotal.film;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ys.atotal.response.FilmWithActorPagedResponse;
import com.ys.atotal.response.PagedResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmWithActorPagedResponse getFilmActorPageResponse (Long page, Long pageSize) {
        List<FilmWithActors> filmWithActorsList = filmRepository.findFilmWithActorsList(page, pageSize);
        return new FilmWithActorPagedResponse(
                new PagedResponse(page, pageSize),
                filmWithActorsList
        );
    }

    public SimpleFilmInfo getSimpleFilmInfo (Long filmId) {
        return filmRepository.findByIdAsSimpleFilmInfo(filmId);
    }
}
