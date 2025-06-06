package com.example.config

import com.example.domain.CafeMenuTable
import com.example.domain.CafeOrderTable
import com.example.domain.CafeUserTable
import com.example.domain.model.CafeOrder
import com.example.shared.CafeOrderStatus
import com.example.shared.dummyQueryList
import com.example.shared.getPropertyBoolean
import com.example.shared.getPropertyString
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.h2.tools.Server
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.LocalDateTime
import java.util.*
import javax.sql.DataSource
import kotlin.random.Random

fun Application.configureDatabase() {
    configureH2()
    connectDatabase()
    if (getPropertyBoolean("db.initData", false)) {
        initData()
    }
}

fun Application.configureH2() {
    val h2Server = Server.createTcpServer(
        "-tcp", "-tcpAllowOthers",
        "-tcpPort", "9092",
    )
    /** 애플리케이션 시작 시 */
    monitor.subscribe(ApplicationStarted) {
        h2Server.start()
        log.info("✅ H2 server started at ${h2Server.url}")
    }

    /** 애플리케이션 종료 시 */
    monitor.subscribe(ApplicationStopped) {
        h2Server.stop()
        log.info("🛑 H2 server stopped.")
    }
}


private fun Application.connectDatabase() {
    // HikariCP 연결 설정
    val config = HikariConfig().apply {
        jdbcUrl = getPropertyString("db.jdbcUrl")
        driverClassName = getPropertyString("db.driverClassName")
        username = "sa"
        password = ""
        maximumPoolSize = 5
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }

    val dataSource: DataSource = HikariDataSource(config)

    // Exposed DB 연결
    Database.connect(dataSource)
}

private fun initData() {
    transaction {
        addLogger(StdOutSqlLogger)


        SchemaUtils.create(
            CafeMenuTable,
            CafeUserTable,
            CafeOrderTable
        )

        execInBatch(dummyQueryList)

        // 테이블 리스트 확인
        val tables = exec("SHOW TABLES") { rs ->
            buildList {
                while (rs.next()) {
                    add(rs.getString(1))
                }
            }
        }
        println("📋 생성된 테이블 목록: $tables")
    }
}

private fun batchInsertOrder(): List<ResultRow> {
    val menuPairs = CafeMenuTable.selectAll()
        .toList()
        .map { it[CafeMenuTable.id].value to it[CafeMenuTable.price] }

    // batch insert for dsl
    val iterator =
        (1..300).map { id ->
            val (menuId, price) = menuPairs.random()
            CafeOrder(
                orderCode = "OC${UUID.randomUUID()}",
                cafeUserId = 1L,
                cafeMenuId = menuId,
                price = price,
                status = CafeOrderStatus.READY,
                orderedAt = LocalDateTime.now().minusDays(Random.nextLong(10))
            )
        }

    return CafeOrderTable.batchInsert(
        iterator,
        shouldReturnGeneratedValues = false,
        body = {
            this[CafeOrderTable.orderCode] = it.orderCode
            this[CafeOrderTable.cafeMenuId] = it.cafeMenuId
            this[CafeOrderTable.cafeUserId] = it.cafeUserId
            this[CafeOrderTable.price] = it.price
            this[CafeOrderTable.status] = it.status
            this[CafeOrderTable.orderedAt] = it.orderedAt
        }
    )
}