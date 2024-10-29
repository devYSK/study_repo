package com.yscorp.reactiver2dbc.interfaces.graphql

import com.yscorp.reactiver2dbc.domain.CustomerEventService
import com.yscorp.reactiver2dbc.dto.CustomerEvent
import kotlinx.coroutines.flow.Flow
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.stereotype.Controller


@Controller
class CustomerEventController(
    private val service: CustomerEventService
) {

    @SubscriptionMapping
    fun customerEvents(): Flow<CustomerEvent> {
        return service.subscribe()
    }

}
