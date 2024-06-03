package org.sight.jooqstart.film;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.generated.tables.pojos.FilmActor;

public record FilmWithActors(Film film, FilmActor filmActor, Actor actor) {

    public Long getFilmId() {
        return film.getFilmId();
    }

    public String getFullActorName() {
        return actor.getFirstName() + " " + actor.getLastName();
    }
}
