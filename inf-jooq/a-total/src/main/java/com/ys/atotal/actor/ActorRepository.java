package com.ys.atotal.actor;

import static com.ys.atotal.jooq.JooqListConditionUtils.*;
import static com.ys.atotal.jooq.JooqStringConditionUtils.*;
import static org.jooq.impl.DSL.*;

import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.JFilm;
import org.jooq.generated.tables.JFilmActor;
import org.jooq.generated.tables.daos.ActorDao;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.generated.tables.records.ActorRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ActorRepository {

    private final DSLContext dslContext;
    private final ActorDao actorDao;
    private final JActor ACTOR = JActor.ACTOR;

    public ActorRepository(DSLContext dslContext, Configuration configuration) {
        this.actorDao = new ActorDao(configuration);
        this.dslContext = dslContext;
    }

    public List<Actor> findByFirstnameAndLastName(String firstName, String lastName) {
        return dslContext.selectFrom(ACTOR)
                .where(
                        ACTOR.FIRST_NAME.eq(firstName),
                        ACTOR.LAST_NAME.eq(lastName)
                ).fetchInto(Actor.class);
    }

    public List<Actor> findByFirstnameOrLastName(String firstName, String lastName) {
        return dslContext.selectFrom(ACTOR)
                .where(
                        ACTOR.FIRST_NAME.eq(firstName).or(ACTOR.LAST_NAME.eq(lastName)), // 이렇게 ,로 구분하면 OR절이 포함된 검색조건과 AND절이 있는 검색조건을 함께 사용할 수 있습니다.
                        ACTOR.FIRST_NAME.eq(firstName)
                ).fetchInto(Actor.class);
    }

    public List<Actor> findByActorIdIn(List<Long> idList) {
        return dslContext.selectFrom(ACTOR)
                .where(inIfNotEmpty(ACTOR.ACTOR_ID, idList))
                .fetchInto(Actor.class);
    }

    // Action로 Film을 그룹핑한다.
    public List<ActorFilmography> findActorFilmography(ActorFilmographySearchOption searchOption) {
        final JFilmActor FILM_ACTOR = JFilmActor.FILM_ACTOR;
        final JFilm FILM = JFilm.FILM;

        Map<Actor, List<Film>> actorListMap = dslContext.select(
                        DSL.row(ACTOR.fields()).as("actor"), // 그룹핑사용시 alias 지정해주는것이 좋다.
                        DSL.row(FILM.fields()).as("film")
                ).from(ACTOR)
                .join(FILM_ACTOR)
                .on(ACTOR.ACTOR_ID.eq(FILM_ACTOR.ACTOR_ID))
                .join(FILM)
                .on(FILM.FILM_ID.eq(FILM_ACTOR.FILM_ID))
                .where(
                        // 배우 full name like 검색
                        containsIfNotBlank(ACTOR.FIRST_NAME.concat(" ").concat(ACTOR.LAST_NAME), searchOption.getActorName()),

                        // 영화 제목 like 검색
                        containsIfNotBlank(FILM.TITLE, searchOption.getFilmTitle())
                )
                .fetchGroups(
                        record -> record.get("actor", Actor.class),
                        record -> record.get("film", Film.class)
                );

        return actorListMap.entrySet().stream()
                .map(entry -> new ActorFilmography(entry.getKey(), entry.getValue()))
                .toList();
    }

    /**
     * 이 부분이 지원되기까지 굉장히 많은 논의가 있었음
     * jOOQ 3.19 부터 지원
     *
     * 참고) https://github.com/jOOQ/jOOQ/issues/2536
     * @return insert 시에 생성된 PK 값이 세팅된 pojo
     */
    public Actor saveByDao(Actor actor) {
        // 이때 PK (actorId)가 actor 객체에 추가됨
        actorDao.insert(actor);
        return actor;
    }

    public ActorRecord saveByRecord(Actor actor) {
        ActorRecord actorRecord = dslContext.newRecord(ACTOR, actor);
        actorRecord.insert();

        // 다만 이 방식은 immutable pojo 에서 사용하기 어려울 수 있음
        // actor.setActorId(actorRecord.getActorId());
        return actorRecord;
    }

    public Actor saveWithReturning(Actor actor) {
        return dslContext.insertInto(ACTOR,
                ACTOR.FIRST_NAME,
                ACTOR.LAST_NAME
            )
            .values(
                actor.getFirstName(),
                actor.getLastName()
            )
            .returning(ACTOR.fields())
            .fetchOneInto(Actor.class);
    }

    public Long saveWithReturningPkOnly(Actor actor) {
        return dslContext.insertInto(ACTOR,
                ACTOR.FIRST_NAME,
                ACTOR.LAST_NAME
            )
            .values(
                actor.getFirstName(),
                actor.getLastName()
            )
            .returningResult(ACTOR.ACTOR_ID)
            .fetchOneInto(Long.class);
    }

    public void bulkInsertWithRows(List<Actor> actorList) {
        var rows = actorList.stream()
            .map(actor -> DSL.row(
                actor.getFirstName(),
                actor.getLastName()
            )).toList();

        dslContext.insertInto(ACTOR,
                ACTOR.FIRST_NAME, ACTOR.LAST_NAME
            ).valuesOfRows(rows)
            .execute();
    }

    public Actor findByActorId(Long actorId) {
        return actorDao.findById(actorId);
    }

    public void update(Actor actor) {
        actorDao.update(actor);
    }

    /**
     * updateWithDto: 개별 필드를 설정하고 업데이트합니다.
     * 업 데이트할 필드가 많고 동적으로 결정되는 경우 updateWithDto가 더 적합
     * updateWithDto는 각 필드를 개별적으로 설정하므로, 업데이트할 필드를 동적으로 결정할 때 유리
     * updateWithDto는 조건문과 set 메서드 호출이 더 많아 코드가 길어질 수 있습니다.
     */
    public int updateWithDto(Long actorId, ActorUpdateRequest request) {
        // noField가 되면 해당 필드는 업데이트 대상이 아님 무시됌
        var firstName = StringUtils.hasText(request.getFirstName()) ? val(request.getFirstName()) : noField(ACTOR.FIRST_NAME);
        var lastName = StringUtils.hasText(request.getLastName()) ? val(request.getLastName()) : noField(ACTOR.LAST_NAME);

        return dslContext.update(ACTOR)
            .set(ACTOR.FIRST_NAME, firstName)
            .set(ACTOR.LAST_NAME, lastName)
            .where(ACTOR.ACTOR_ID.eq(actorId))
            .execute();
    }

    /**
     * updateWithRecord: 레코드 객체를 생성하고 설정한 후, 전체 레코드를 업데이트\
     * updateWithRecord는 레코드 객체를 사용하므로 코드가 더 간결할 수 있다.
     *
     * 레코드 객체를 사용하여 여러 필드를 한 번에 설정하고 업데이트하는 것이 더 간편할 때.
     * 코드의 간결성과 가독성을 중시할 때.
     * 설정할 필드가 많지 않고 대부분의 필드를 업데이트해야 하는 경우.
     */
    public int updateWithRecord(Long actorId, ActorUpdateRequest request) {
        var record = dslContext.newRecord(ACTOR);

        if (StringUtils.hasText(request.getFirstName())) {
            record.setFirstName(request.getFirstName());
        }

        if (StringUtils.hasText(request.getLastName())) {
            record.setLastName(request.getLastName());
        }

        return dslContext.update(ACTOR)
            .set(record)
            .where(ACTOR.ACTOR_ID.eq(actorId))
            .execute();
        // 또는
        // record.setActorId(actorId);
        // return record.update();
    }

    public int delete(Long actorId) {
        return dslContext.deleteFrom(ACTOR)
            .where(ACTOR.ACTOR_ID.eq(actorId))
            .execute();
    }

    public int deleteWithActiveRecord(Long actorId) {
        ActorRecord actorRecord = dslContext.newRecord(ACTOR);
        actorRecord.setActorId(actorId);
        return actorRecord.delete();
    }

    public ActorRecord findRecordByActorId(Long actorId) {
        return dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(actorId));
    }


}
