/*
 * Copyright (c) 2017 by Todd Ginsberg
 */

package com.ginsberg.counter.api

import com.ginsberg.counter.data.CounterRepository
import com.ginsberg.counter.model.CounterDown
import com.ginsberg.counter.model.CounterState
import com.ginsberg.counter.model.CounterUp
import com.ginsberg.counter.service.EventBus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import reactor.core.publisher.Mono


@Component
class CounterHandler(private val eventBus: EventBus,
                     private val counterRepository: CounterRepository) {

    fun get(serverRequest: ServerRequest): Mono<ServerResponse> =
        ServerResponse
            .ok()
            .body(
                counterRepository.get()
                    .map { CounterState(it) }
            )

    fun up(serverRequest: ServerRequest): Mono<ServerResponse> =
        ServerResponse
            .ok()
            .body(
                counterRepository.up()
                    .map { CounterState(it) }
                    .doOnNext { eventBus.publish(CounterUp(it.value)) }
            )

    fun down(serverRequest: ServerRequest): Mono<ServerResponse> =
        ServerResponse
            .ok()
            .body(
                counterRepository.down()
                    .map { CounterState(it) }
                    .doOnNext { eventBus.publish(CounterDown(it.value)) }
            )

    fun stream(serverRequest: ServerRequest): Mono<ServerResponse> =
        ServerResponse
            .ok()
            .bodyToServerSentEvents(eventBus.subscribe())

}