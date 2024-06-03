package com.ys.atotal.response;

import java.util.List;

import com.ys.atotal.film.FilmWithActors;

import lombok.Getter;

@Getter
public class FilmWithActorPagedResponse {

    private final PagedResponse page;
    private final List<FilmActorResponse> filmActor;

    public FilmWithActorPagedResponse(PagedResponse page, List<FilmWithActors> filmWithActors) {
        this.page = page;
        this.filmActor = filmWithActors.stream()
                .map(FilmActorResponse::new)
                .toList();
    }

    @Getter
    public static class FilmActorResponse {

        private final String filmTitle;
        private final int filmLength;
        private final String actorFullName;

        public FilmActorResponse(FilmWithActors filmWithActors) {
            this.filmTitle = filmWithActors.getFilm().getTitle();
            this.filmLength = filmWithActors.getFilm().getLength();
            this.actorFullName = filmWithActors.getFullActorName();
        }
    }
}
