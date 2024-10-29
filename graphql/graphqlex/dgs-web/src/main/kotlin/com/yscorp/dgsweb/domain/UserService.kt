package com.yscorp.dgsweb.domain

import com.yscorp.dgsweb.codegen.types.PageInfo
import com.yscorp.dgsweb.codegen.types.UserConnection
import com.yscorp.dgsweb.codegen.types.UserEdge
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    /**
     * 커서 기반으로 유저 목록을 조회합니다.
     * @param cursor 마지막으로 조회된 유저의 ID (nullable)
     * @param pageSize 한 페이지에서 가져올 데이터의 크기
     */
    fun getUsersWithCursorPagination(cursor: Long?, pageSize: Int): UserConnection {
        val users = if (cursor == null) {
            // 처음 요청 시, 커서가 없으므로 최신순으로 데이터를 가져옴
            userRepository.findTopUsers(pageSize)
        } else {
            // 커서가 있을 경우, 해당 커서 이후의 데이터를 가져옴
            userRepository.findUsersAfterCursor(cursor, pageSize)
        }

        // 다음 페이지가 있는지 확인
        val hasNext = users.size == pageSize
        val edges = users.map { user ->
            UserEdge(
                cursor = user.id.toString(),
                node = UserDTO(user.id, user.name, user.email, user.createdAt.toString())
            )
        }

        return UserConnection(
            edges = edges,
            pageInfo = PageInfo(
                hasNextPage = hasNext,
                hasPreviousPage = cursor != null,
                startCursor = edges.firstOrNull()?.cursor,
                endCursor = edges.lastOrNull()?.cursor
            )
        )
    }
}
