package com.yscorp.dgsweb.interfaces.scalar

import com.netflix.graphql.dgs.DgsScalar
import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.Value
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import org.springframework.web.multipart.MultipartFile
import java.util.*


class UploadCoercing : Coercing<MultipartFile, Any> {

    // serialize 메서드 구현 (서버 -> 클라이언트)
    override fun serialize(
        dataFetcherResult: Any,
        graphQLContext: GraphQLContext,
        locale: Locale
    ): Any {
        throw CoercingSerializeException("Upload 타입은 직렬화할 수 없습니다.")
    }

    // parseValue 메서드 구현 (클라이언트 -> 서버, 변수 사용)
    override fun parseValue(
        input: Any,
        graphQLContext: GraphQLContext,
        locale: Locale
    ): MultipartFile {
        if (input is MultipartFile) {
            return input
        }
        throw CoercingParseValueException("Expected type 'MultipartFile' but was '${input::class.simpleName}'.")
    }

    // parseLiteral 메서드 구현 (클라이언트 -> 서버, 리터럴 사용)
    override fun parseLiteral(
        input: Value<*>,
        variables: CoercedVariables,
        graphQLContext: GraphQLContext,
        locale: Locale
    ): MultipartFile {
        throw CoercingParseLiteralException("Parsing literals is not supported for 'Upload' type.")
    }

    // valueToLiteral 메서드 구현 (선택 사항)
    override fun valueToLiteral(
        input: Any,
        graphQLContext: GraphQLContext,
        locale: Locale
    ): Value<*> {
        throw UnsupportedOperationException("valueToLiteral is not supported for 'Upload' type.")
    }
}

@DgsScalar(name = "UploadFile")
class UploadScalar : Coercing<MultipartFile, Any> by UploadCoercing()