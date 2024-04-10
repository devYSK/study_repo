package com.example.couponcore.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = -1421122493L;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final EnumPath<CouponType> couponType = createEnum("couponType", CouponType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dateCreated = _super.dateCreated;

    public final DateTimePath<java.time.LocalDateTime> dateIssueEnd = createDateTime("dateIssueEnd", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> dateIssueStart = createDateTime("dateIssueStart", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dateUpdated = _super.dateUpdated;

    public final NumberPath<Integer> discountAmount = createNumber("discountAmount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> issuedQuantity = createNumber("issuedQuantity", Integer.class);

    public final NumberPath<Integer> minAvailableAmount = createNumber("minAvailableAmount", Integer.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> totalQuantity = createNumber("totalQuantity", Integer.class);

    public QCoupon(String variable) {
        super(Coupon.class, forVariable(variable));
    }

    public QCoupon(Path<? extends Coupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoupon(PathMetadata metadata) {
        super(Coupon.class, metadata);
    }

}

