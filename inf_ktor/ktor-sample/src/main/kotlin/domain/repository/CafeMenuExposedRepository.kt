package com.example.domain.repository

import com.example.domain.CafeMenuTable
import com.example.domain.model.CafeMenu
import com.example.shared.CafeMenuCategory
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class CafeMenuExposedRepository {

    fun save(menu: CafeMenu): CafeMenu = transaction {
        val id = CafeMenuTable.insertAndGetId {
            it[name] = menu.name
            it[price] = menu.price
            it[category] = menu.category
            it[image] = menu.image
        }
        menu.copy(id = id.value)
    }

    fun findById(id: Long): CafeMenu? = transaction {
        CafeMenuTable.select(CafeMenuTable.id eq id)
            .map(::toDomain)
            .singleOrNull()
    }

    fun findAll(): List<CafeMenu> = transaction {
        CafeMenuTable.selectAll().map(::toDomain)
    }

    fun delete(id: Long): Boolean = transaction {
        CafeMenuTable.deleteWhere { CafeMenuTable.id eq id } > 0
    }

    // 동적 쿼리 예시: 가격 필터
    fun findByMinPrice(minPrice: Int): List<CafeMenu> = transaction {
        CafeMenuTable.select(CafeMenuTable.price greaterEq minPrice)
            .map(::toDomain)
    }

    // 네이티브 쿼리 예시
    fun findByNative(): List<CafeMenu> = transaction {
        exec("SELECT * FROM cafe_menu WHERE price > 10000") {
            val result = mutableListOf<CafeMenu>()
            while (it.next()) {
                result.add(
                    CafeMenu(
                        id = it.getLong("id"),
                        name = it.getString("menu_name"),
                        price = it.getInt("price"),
                        category = CafeMenuCategory.valueOf(it.getString("category")),
                        image = it.getString("image")
                    )
                )
            }
            result
        } ?: emptyList()
    }

    private fun toDomain(row: ResultRow) = CafeMenu(
        id = row[CafeMenuTable.id].value,
        name = row[CafeMenuTable.name],
        price = row[CafeMenuTable.price],
        category = row[CafeMenuTable.category],
        image = row[CafeMenuTable.image]
    )
}
