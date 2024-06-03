package com.ys.atotal.actor;

import java.util.List;

import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.pojos.Film;

import lombok.Getter;

@Getter
public class ActorFilmography {

    private final Actor actor;
    private final List<Film> filmList;

    public ActorFilmography(Actor actor, List<Film> filmList) {
        this.actor = actor;
        this.filmList = filmList;
    }
}
