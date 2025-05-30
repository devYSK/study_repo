/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.processing.Generated;

import org.jooq.Configuration;
import org.jooq.generated.tables.JAddress;
import org.jooq.generated.tables.pojos.Address;
import org.jooq.generated.tables.records.AddressRecord;
import org.jooq.impl.AutoConverter;
import org.jooq.impl.DAOImpl;
import org.jooq.types.UInteger;


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
public class AddressDao extends DAOImpl<AddressRecord, Address, Long> {

    /**
     * Create a new AddressDao without any configuration
     */
    public AddressDao() {
        super(JAddress.ADDRESS, Address.class);
    }

    /**
     * Create a new AddressDao with an attached configuration
     */
    public AddressDao(Configuration configuration) {
        super(JAddress.ADDRESS, Address.class, configuration);
    }

    @Override
    public Long getId(Address object) {
        return object.getAddressId();
    }

    /**
     * Fetch records that have <code>address_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Address> fetchRangeOfJAddressId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JAddress.ADDRESS.ADDRESS_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>address_id IN (values)</code>
     */
    public List<Address> fetchByJAddressId(Long... values) {
        return fetch(JAddress.ADDRESS.ADDRESS_ID, values);
    }

    /**
     * Fetch a unique record that has <code>address_id = value</code>
     */
    public Address fetchOneByJAddressId(Long value) {
        return fetchOne(JAddress.ADDRESS.ADDRESS_ID, value);
    }

    /**
     * Fetch a unique record that has <code>address_id = value</code>
     */
    public Optional<Address> fetchOptionalByJAddressId(Long value) {
        return fetchOptional(JAddress.ADDRESS.ADDRESS_ID, value);
    }

    /**
     * Fetch records that have <code>address BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Address> fetchRangeOfJAddress(String lowerInclusive, String upperInclusive) {
        return fetchRange(JAddress.ADDRESS.ADDRESS_, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>address IN (values)</code>
     */
    public List<Address> fetchByJAddress(String... values) {
        return fetch(JAddress.ADDRESS.ADDRESS_, values);
    }

    /**
     * Fetch records that have <code>address2 BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Address> fetchRangeOfJAddress2(String lowerInclusive, String upperInclusive) {
        return fetchRange(JAddress.ADDRESS.ADDRESS2, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>address2 IN (values)</code>
     */
    public List<Address> fetchByJAddress2(String... values) {
        return fetch(JAddress.ADDRESS.ADDRESS2, values);
    }

    /**
     * Fetch records that have <code>district BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Address> fetchRangeOfJDistrict(String lowerInclusive, String upperInclusive) {
        return fetchRange(JAddress.ADDRESS.DISTRICT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>district IN (values)</code>
     */
    public List<Address> fetchByJDistrict(String... values) {
        return fetch(JAddress.ADDRESS.DISTRICT, values);
    }

    /**
     * Fetch records that have <code>city_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Address> fetchRangeOfJCityId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JAddress.ADDRESS.CITY_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>city_id IN (values)</code>
     */
    public List<Address> fetchByJCityId(Long... values) {
        return fetch(JAddress.ADDRESS.CITY_ID, values);
    }

    /**
     * Fetch records that have <code>postal_code BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Address> fetchRangeOfJPostalCode(String lowerInclusive, String upperInclusive) {
        return fetchRange(JAddress.ADDRESS.POSTAL_CODE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>postal_code IN (values)</code>
     */
    public List<Address> fetchByJPostalCode(String... values) {
        return fetch(JAddress.ADDRESS.POSTAL_CODE, values);
    }

    /**
     * Fetch records that have <code>phone BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Address> fetchRangeOfJPhone(String lowerInclusive, String upperInclusive) {
        return fetchRange(JAddress.ADDRESS.PHONE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>phone IN (values)</code>
     */
    public List<Address> fetchByJPhone(String... values) {
        return fetch(JAddress.ADDRESS.PHONE, values);
    }

    /**
     * Fetch records that have <code>last_update BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Address> fetchRangeOfJLastUpdate(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(JAddress.ADDRESS.LAST_UPDATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>last_update IN (values)</code>
     */
    public List<Address> fetchByJLastUpdate(LocalDateTime... values) {
        return fetch(JAddress.ADDRESS.LAST_UPDATE, values);
    }
}
