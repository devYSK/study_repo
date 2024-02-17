package com.fastcamedu.hellographql.config;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

import com.netflix.graphql.dgs.DgsScalar;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
@DgsScalar(name = "BigDecimal")
public class BigDecimalScalar implements Coercing<BigDecimal, String> {

	@Override
	public String serialize(@NotNull Object dataFetcherResult) throws CoercingSerializeException {
		if (dataFetcherResult instanceof BigDecimal) {
			return ((BigDecimal)dataFetcherResult).toPlainString();
		} else {
			throw new CoercingSerializeException("Not a valid BigDecimal");
		}
	}

	@Override
	public @NotNull BigDecimal parseValue(@NotNull Object input) throws CoercingParseValueException {
		try {
			return new BigDecimal(input.toString());
		} catch (NumberFormatException e) {
			throw new CoercingParseValueException("Value is not a valid BigDecimal format", e);
		}
	}

	@Override
	public @NotNull BigDecimal parseLiteral(@NotNull Object input) throws CoercingParseLiteralException {
		if (input instanceof StringValue) {
			try {
				return new BigDecimal(((StringValue) input).getValue());
			} catch (NumberFormatException e) {
				throw new CoercingParseLiteralException("Value is not a valid BigDecimal format", e);
			}
		}
		throw new CoercingParseLiteralException("Value is not a valid BigDecimal format");
	}
}
