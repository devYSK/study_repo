package com.example.controller

import com.example.route.menuRoute
import com.example.route.orderRoute
import com.example.route.userRoute
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            userRoute()
            menuRoute()
            orderRoute()
        }

        singlePageApplication {
            react("frontend")
        }
    }
}
