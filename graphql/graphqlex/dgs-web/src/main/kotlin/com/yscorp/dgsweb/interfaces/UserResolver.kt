package com.yscorp.dgsweb.interfaces

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.yscorp.dgsweb.codegen.types.UserConnection
import com.yscorp.dgsweb.domain.UserService

@DgsComponent
class UserResolver(
    private val userService: UserService
) {

    /**
     * 커서 기반 페이지네이션으로 유저 목록을 조회하는 GraphQL 쿼리
     * @param first 페이지 크기
     * @param after 커서 (마지막으로 가져온 유저의 ID)
     */
    @DgsQuery
    fun users(
        @InputArgument first: Int,
        @InputArgument after: String?
    ): UserConnection {
        val cursor = after?.toLongOrNull()
        return userService.getUsersWithCursorPagination(cursor, first)
    }
}