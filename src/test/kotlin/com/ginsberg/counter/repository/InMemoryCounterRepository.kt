/*
 * Copyright (c) 2017 by Todd Ginsberg
 */

package com.ginsberg.counter.repository

import com.ginsberg.counter.data.CounterRepository
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicLong

/**
 * A version of CounterRepository to be used for testing.
 */
@Component
@Primary // Will only be applied when in test mode.
class InMemoryCounterRepository : CounterRepository {
    private val counter = AtomicLong(0L)

    override fun up(): Mono<Long> =
        Mono.just(counter.incrementAndGet())

    override fun down(): Mono<Long> =
        Mono.just(counter.decrementAndGet())

    override fun get(): Mono<Long> =
        Mono.just(counter.get())

}
