/*
 * This file is generated by jOOQ.
 */
package com.uno.getinline.tables.records;


import com.uno.getinline.constant.PlaceType;
import com.uno.getinline.tables.Place;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PlaceRecord extends UpdatableRecordImpl<PlaceRecord> implements Record9<Long, PlaceType, String, String, String, Integer, String, LocalDateTime, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>getinline.place.id</code>.
     */
    public PlaceRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>getinline.place.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>getinline.place.place_type</code>.
     */
    public PlaceRecord setPlaceType(PlaceType value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>getinline.place.place_type</code>.
     */
    public PlaceType getPlaceType() {
        return (PlaceType) get(1);
    }

    /**
     * Setter for <code>getinline.place.place_name</code>.
     */
    public PlaceRecord setPlaceName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>getinline.place.place_name</code>.
     */
    public String getPlaceName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>getinline.place.address</code>.
     */
    public PlaceRecord setAddress(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>getinline.place.address</code>.
     */
    public String getAddress() {
        return (String) get(3);
    }

    /**
     * Setter for <code>getinline.place.phone_number</code>.
     */
    public PlaceRecord setPhoneNumber(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>getinline.place.phone_number</code>.
     */
    public String getPhoneNumber() {
        return (String) get(4);
    }

    /**
     * Setter for <code>getinline.place.capacity</code>.
     */
    public PlaceRecord setCapacity(Integer value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>getinline.place.capacity</code>.
     */
    public Integer getCapacity() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>getinline.place.memo</code>.
     */
    public PlaceRecord setMemo(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>getinline.place.memo</code>.
     */
    public String getMemo() {
        return (String) get(6);
    }

    /**
     * Setter for <code>getinline.place.created_at</code>.
     */
    public PlaceRecord setCreatedAt(LocalDateTime value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>getinline.place.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(7);
    }

    /**
     * Setter for <code>getinline.place.modified_at</code>.
     */
    public PlaceRecord setModifiedAt(LocalDateTime value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>getinline.place.modified_at</code>.
     */
    public LocalDateTime getModifiedAt() {
        return (LocalDateTime) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<Long, PlaceType, String, String, String, Integer, String, LocalDateTime, LocalDateTime> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<Long, PlaceType, String, String, String, Integer, String, LocalDateTime, LocalDateTime> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Place.PLACE.ID;
    }

    @Override
    public Field<PlaceType> field2() {
        return Place.PLACE.PLACE_TYPE;
    }

    @Override
    public Field<String> field3() {
        return Place.PLACE.PLACE_NAME;
    }

    @Override
    public Field<String> field4() {
        return Place.PLACE.ADDRESS;
    }

    @Override
    public Field<String> field5() {
        return Place.PLACE.PHONE_NUMBER;
    }

    @Override
    public Field<Integer> field6() {
        return Place.PLACE.CAPACITY;
    }

    @Override
    public Field<String> field7() {
        return Place.PLACE.MEMO;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return Place.PLACE.CREATED_AT;
    }

    @Override
    public Field<LocalDateTime> field9() {
        return Place.PLACE.MODIFIED_AT;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public PlaceType component2() {
        return getPlaceType();
    }

    @Override
    public String component3() {
        return getPlaceName();
    }

    @Override
    public String component4() {
        return getAddress();
    }

    @Override
    public String component5() {
        return getPhoneNumber();
    }

    @Override
    public Integer component6() {
        return getCapacity();
    }

    @Override
    public String component7() {
        return getMemo();
    }

    @Override
    public LocalDateTime component8() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime component9() {
        return getModifiedAt();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public PlaceType value2() {
        return getPlaceType();
    }

    @Override
    public String value3() {
        return getPlaceName();
    }

    @Override
    public String value4() {
        return getAddress();
    }

    @Override
    public String value5() {
        return getPhoneNumber();
    }

    @Override
    public Integer value6() {
        return getCapacity();
    }

    @Override
    public String value7() {
        return getMemo();
    }

    @Override
    public LocalDateTime value8() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime value9() {
        return getModifiedAt();
    }

    @Override
    public PlaceRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public PlaceRecord value2(PlaceType value) {
        setPlaceType(value);
        return this;
    }

    @Override
    public PlaceRecord value3(String value) {
        setPlaceName(value);
        return this;
    }

    @Override
    public PlaceRecord value4(String value) {
        setAddress(value);
        return this;
    }

    @Override
    public PlaceRecord value5(String value) {
        setPhoneNumber(value);
        return this;
    }

    @Override
    public PlaceRecord value6(Integer value) {
        setCapacity(value);
        return this;
    }

    @Override
    public PlaceRecord value7(String value) {
        setMemo(value);
        return this;
    }

    @Override
    public PlaceRecord value8(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public PlaceRecord value9(LocalDateTime value) {
        setModifiedAt(value);
        return this;
    }

    @Override
    public PlaceRecord values(Long value1, PlaceType value2, String value3, String value4, String value5, Integer value6, String value7, LocalDateTime value8, LocalDateTime value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PlaceRecord
     */
    public PlaceRecord() {
        super(Place.PLACE);
    }

    /**
     * Create a detached, initialised PlaceRecord
     */
    public PlaceRecord(Long id, PlaceType placeType, String placeName, String address, String phoneNumber, Integer capacity, String memo, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        super(Place.PLACE);

        setId(id);
        setPlaceType(placeType);
        setPlaceName(placeName);
        setAddress(address);
        setPhoneNumber(phoneNumber);
        setCapacity(capacity);
        setMemo(memo);
        setCreatedAt(createdAt);
        setModifiedAt(modifiedAt);
    }
}
