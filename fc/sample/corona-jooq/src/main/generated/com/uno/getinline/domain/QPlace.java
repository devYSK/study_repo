package com.uno.getinline.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlace is a Querydsl query type for Place
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPlace extends EntityPathBase<Place> {

    private static final long serialVersionUID = -1415082527L;

    public static final QPlace place = new QPlace("place");

    public final StringPath address = createString("address");

    public final SetPath<AdminPlaceMap, QAdminPlaceMap> adminPlaceMaps = this.<AdminPlaceMap, QAdminPlaceMap>createSet("adminPlaceMaps", AdminPlaceMap.class, QAdminPlaceMap.class, PathInits.DIRECT2);

    public final NumberPath<Integer> capacity = createNumber("capacity", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final SetPath<Event, QEvent> events = this.<Event, QEvent>createSet("events", Event.class, QEvent.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath placeName = createString("placeName");

    public final EnumPath<com.uno.getinline.constant.PlaceType> placeType = createEnum("placeType", com.uno.getinline.constant.PlaceType.class);

    public QPlace(String variable) {
        super(Place.class, forVariable(variable));
    }

    public QPlace(Path<? extends Place> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPlace(PathMetadata metadata) {
        super(Place.class, metadata);
    }

}

