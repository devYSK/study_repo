package com.yscorp.reactiver2dbc.domain

import com.yscorp.reactiver2dbc.dto.CustomerEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Service
class CustomerEventService {

    private val sink: Sinks.Many<CustomerEvent> = Sinks.many()
        .multicast()
        .onBackpressureBuffer<CustomerEvent>()
    private val flux: Flux<CustomerEvent> = sink.asFlux().cache(0)

    fun emitEvent(event: CustomerEvent) {
        sink.tryEmitNext(event)
    }

    fun subscribe(): Flow<CustomerEvent> {
        return this.flux.asFlow()
    }

}
