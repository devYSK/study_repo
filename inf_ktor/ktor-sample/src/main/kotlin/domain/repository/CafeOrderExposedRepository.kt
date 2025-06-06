package com.example.domain.repository

import com.example.domain.CafeMenuTable
import com.example.domain.CafeOrderTable
import com.example.domain.CafeUserTable
import com.example.domain.model.CafeOrder
import com.example.shared.CafeOrderStatus
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class CafeOrderExposedRepository {

    fun save(order: CafeOrder): CafeOrder = transaction {
        val id = CafeOrderTable.insertAndGetId {
            it[orderCode] = order.orderCode
            it[cafeUserId] = EntityID(order.cafeUserId, CafeUserTable)
            it[cafeMenuId] = EntityID(order.cafeMenuId, CafeMenuTable)
            it[price] = order.price
            it[status] = order.status
            it[orderedAt] = order.orderedAt
        }
        order.copy(id = id.value)
    }

    fun findById(id: Long): CafeOrder? = transaction {
        CafeOrderTable.select(CafeOrderTable.id eq id)
            .map(::toDomain)
            .singleOrNull()
    }

    fun findAll(): List<CafeOrder> = transaction {
        CafeOrderTable.selectAll().map(::toDomain)
    }

    fun delete(id: Long): Boolean = transaction {
        CafeOrderTable.deleteWhere { CafeOrderTable.id eq id } > 0
    }

    // 동적 쿼리: 특정 사용자 주문 조회
    fun findByUser(userId: Long): List<CafeOrder> = transaction {
        CafeOrderTable.select(CafeOrderTable.cafeUserId eq EntityID(userId, CafeUserTable))
            .map(::toDomain)
    }

    // 네이티브 쿼리 예시
    fun findByNative(): List<CafeOrder> = transaction {
        exec("SELECT * FROM cafe_order WHERE status = 'PENDING'") {
            val result = mutableListOf<CafeOrder>()
            while (it.next()) {
                result.add(
                    CafeOrder(
                        id = it.getLong("id"),
                        orderCode = it.getString("order_code"),
                        cafeUserId = it.getLong("cafe_user_id"),
                        cafeMenuId = it.getLong("cafe_menu_id"),
                        price = it.getInt("price"),
                        status = CafeOrderStatus.valueOf(it.getString("status")),
                        orderedAt = it.getTimestamp("ordered_at").toLocalDateTime()
                    )
                )
            }
            result
        } ?: emptyList()
    }

    private fun toDomain(row: ResultRow) = CafeOrder(
        id = row[CafeOrderTable.id].value,
        orderCode = row[CafeOrderTable.orderCode],
        cafeUserId = row[CafeOrderTable.cafeUserId].value,
        cafeMenuId = row[CafeOrderTable.cafeMenuId].value,
        price = row[CafeOrderTable.price],
        status = row[CafeOrderTable.status],
        orderedAt = row[CafeOrderTable.orderedAt]
    )
}
