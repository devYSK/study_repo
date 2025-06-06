package com.example.domain

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.core.statements.UpdateStatement
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction


interface ExposedCrudRepository<TABLE : LongIdTable, DOMAIN : BaseModel> : CrudRepository<DOMAIN> {
    val table: TABLE
    override fun create(domain: DOMAIN): DOMAIN = dbQuery {
        val id = table.insertAndGetId(toRow(domain))
        domain.id = id.value
        domain
    }

    override fun findAll(): List<DOMAIN> = dbQuery {
        table.selectAll().map { toDomain(it) }
    }

    override fun read(id: Long): DOMAIN? = dbQuery {
        table.selectAll().where { table.id eq id }
            .map { toDomain(it) }
            .singleOrNull()
    }

    override fun update(domain: DOMAIN): DOMAIN = dbQuery {
        table.update(
            where = { table.id eq domain.id },
            body = updateRow(domain)
        )
        domain
    }

    override fun delete(domain: DOMAIN) = dbQuery {
        table.deleteWhere { table.id eq domain.id }
        return@dbQuery
    }

    override fun delete(id: Long) = dbQuery {
        table.deleteWhere { table.id eq id }
        return@dbQuery
    }

    fun toRow(domain: DOMAIN): TABLE.(UpdateBuilder<*>) -> Unit
//    fun toRow(domain: DOMAIN): TABLE.(InsertStatement<EntityID<Long>>) -> Unit
    fun toDomain(row: ResultRow): DOMAIN
    fun updateRow(domain: DOMAIN): TABLE.(UpdateStatement) -> Unit

    fun <T> dbQuery(block: () -> T): T = transaction {
        addLogger(StdOutSqlLogger)
        block()
    }
}
