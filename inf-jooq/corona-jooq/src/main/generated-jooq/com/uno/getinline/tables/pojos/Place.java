/*
 * This file is generated by jOOQ.
 */
package com.uno.getinline.tables.pojos;


import com.uno.getinline.constant.PlaceType;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Place implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long          id;
    private final PlaceType     placeType;
    private final String        placeName;
    private final String        address;
    private final String        phoneNumber;
    private final Integer       capacity;
    private final String        memo;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public Place(Place value) {
        this.id = value.id;
        this.placeType = value.placeType;
        this.placeName = value.placeName;
        this.address = value.address;
        this.phoneNumber = value.phoneNumber;
        this.capacity = value.capacity;
        this.memo = value.memo;
        this.createdAt = value.createdAt;
        this.modifiedAt = value.modifiedAt;
    }

    public Place(
        Long          id,
        PlaceType     placeType,
        String        placeName,
        String        address,
        String        phoneNumber,
        Integer       capacity,
        String        memo,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
    ) {
        this.id = id;
        this.placeType = placeType;
        this.placeName = placeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.capacity = capacity;
        this.memo = memo;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    /**
     * Getter for <code>getinline.place.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for <code>getinline.place.place_type</code>.
     */
    public PlaceType getPlaceType() {
        return this.placeType;
    }

    /**
     * Getter for <code>getinline.place.place_name</code>.
     */
    public String getPlaceName() {
        return this.placeName;
    }

    /**
     * Getter for <code>getinline.place.address</code>.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Getter for <code>getinline.place.phone_number</code>.
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Getter for <code>getinline.place.capacity</code>.
     */
    public Integer getCapacity() {
        return this.capacity;
    }

    /**
     * Getter for <code>getinline.place.memo</code>.
     */
    public String getMemo() {
        return this.memo;
    }

    /**
     * Getter for <code>getinline.place.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Getter for <code>getinline.place.modified_at</code>.
     */
    public LocalDateTime getModifiedAt() {
        return this.modifiedAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Place (");

        sb.append(id);
        sb.append(", ").append(placeType);
        sb.append(", ").append(placeName);
        sb.append(", ").append(address);
        sb.append(", ").append(phoneNumber);
        sb.append(", ").append(capacity);
        sb.append(", ").append(memo);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(modifiedAt);

        sb.append(")");
        return sb.toString();
    }
}
