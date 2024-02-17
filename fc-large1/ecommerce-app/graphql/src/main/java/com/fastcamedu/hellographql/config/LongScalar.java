package com.fastcamedu.hellographql.config;

import org.jetbrains.annotations.NotNull;

import com.netflix.graphql.dgs.DgsScalar;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
//
// @DgsScalar(name = "Long")
// public class LongScalar implements Coercing<Long, String> {
// 	@Override
// 	public String serialize(@NotNull Object dataFetcherResult) throws CoercingSerializeException {
// 		return null;
// 	}
//
// 	@Override
// 	public @NotNull Long parseValue(@NotNull Object input) throws CoercingParseValueException {
// 		return null;
// 	}
//
// 	@Override
// 	public @NotNull Long parseLiteral(@NotNull Object input) throws CoercingParseLiteralException {
// 		return null;
// 	}
//
// }