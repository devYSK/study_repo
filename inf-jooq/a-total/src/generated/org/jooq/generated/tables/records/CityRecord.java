/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import org.jooq.Record1;
import org.jooq.generated.tables.JCity;
import org.jooq.generated.tables.pojos.City;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.19.5",
        "schema version:1.0.03"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CityRecord extends UpdatableRecordImpl<CityRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>sakila.city.city_id</code>.
     */
    public CityRecord setCityId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>sakila.city.city_id</code>.
     */
    public Long getCityId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>sakila.city.city</code>.
     */
    public CityRecord setCity(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>sakila.city.city</code>.
     */
    public String getCity() {
        return (String) get(1);
    }

    /**
     * Setter for <code>sakila.city.country_id</code>.
     */
    public CityRecord setCountryId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>sakila.city.country_id</code>.
     */
    public Long getCountryId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>sakila.city.last_update</code>.
     */
    public CityRecord setLastUpdate(LocalDateTime value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>sakila.city.last_update</code>.
     */
    public LocalDateTime getLastUpdate() {
        return (LocalDateTime) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CityRecord
     */
    public CityRecord() {
        super(JCity.CITY);
    }

    /**
     * Create a detached, initialised CityRecord
     */
    public CityRecord(Long cityId, String city, Long countryId, LocalDateTime lastUpdate) {
        super(JCity.CITY);

        setCityId(cityId);
        setCity(city);
        setCountryId(countryId);
        setLastUpdate(lastUpdate);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised CityRecord
     */
    public CityRecord(City value) {
        super(JCity.CITY);

        if (value != null) {
            setCityId(value.getCityId());
            setCity(value.getCity());
            setCountryId(value.getCountryId());
            setLastUpdate(value.getLastUpdate());
            resetChangedOnNotNull();
        }
    }
}
