package org.sight.jooqstart.web.response;

import lombok.Getter;
import lombok.ToString;

import org.sight.jooqstart.film.FilmWithActors;

import java.util.List;

@Getter
@ToString
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
    @ToString
    public static class FilmActorResponse {

        private final String filmTitle;
        private final int filmLength;
        private final String actorFullName;

        public FilmActorResponse(FilmWithActors filmWithActors) {
            this.filmTitle = filmWithActors.film().getTitle();
            this.filmLength = filmWithActors.film().getLength();
            this.actorFullName = filmWithActors.getFullActorName();
        }
    }
}
