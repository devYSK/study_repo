package com.example.domain.repository

import com.example.domain.CafeMenuTable
import com.example.domain.ExposedCrudRepository
import com.example.domain.model.CafeMenu
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.core.statements.UpdateStatement

class CafeMenuRepository(
    override val table: CafeMenuTable,
) : ExposedCrudRepository<CafeMenuTable, CafeMenu> {

    override fun toRow(domain: CafeMenu): CafeMenuTable.(UpdateBuilder<*>) -> Unit = {
        if (domain.id != null) {
            it[id] = domain.id!!
        }
        it[name] = domain.name
        it[price] = domain.price
        it[category] = domain.category
        it[image] = domain.image
    }

    override fun toDomain(row: ResultRow): CafeMenu {
        return CafeMenu(
            name = row[CafeMenuTable.name],
            price = row[CafeMenuTable.price],
            category = row[CafeMenuTable.category],
            image = row[CafeMenuTable.image]
        ).apply {
            id = row[CafeMenuTable.id].value
        }
    }

    override fun updateRow(domain: CafeMenu): CafeMenuTable.(UpdateStatement) -> Unit = {
        it[name] = domain.name
        it[price] = domain.price
        it[category] = domain.category
    }
}
