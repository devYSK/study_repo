package com.fastcamedu.hellographql.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.netflix.graphql.dgs.DgsScalar;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

@DgsScalar(name = "DateTime")
public class DateTimeScalar implements Coercing<LocalDateTime, String> {

	@Override
	public String serialize(Object dataFetcherResult) {
		if (dataFetcherResult instanceof LocalDateTime) {
			return ((LocalDateTime)dataFetcherResult).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}
		throw new CoercingSerializeException("Unable to serialize " + dataFetcherResult + " as a LocalDateTime");
	}

	@Override
	public LocalDateTime parseValue(Object input) {
		try {
			if (input instanceof String) {
				return LocalDateTime.parse((String)input, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			}
			throw new CoercingParseValueException("Unable to parse variable value " + input + " as a LocalDateTime");
		} catch (DateTimeParseException e) {
			throw new CoercingParseValueException("Unable to parse variable value " + input + " as a LocalDateTime", e);
		}
	}

	@Override
	public LocalDateTime parseLiteral(Object input) {
		if (!(input instanceof String)) {
			throw new CoercingParseLiteralException("Value is not a string");
		}
		try {
			return LocalDateTime.parse((String)input, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		} catch (DateTimeParseException e) {
			throw new CoercingParseLiteralException("Unable to parse " + input + " as a LocalDateTime", e);
		}
	}

}