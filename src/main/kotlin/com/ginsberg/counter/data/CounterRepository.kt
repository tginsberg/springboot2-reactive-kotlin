/*
 * Copyright (c) 2017 by Todd Ginsberg
 */

package com.ginsberg.counter.data

import reactor.core.publisher.Mono


interface CounterRepository {
    fun up(): Mono<Long>
    fun down(): Mono<Long>
    fun get(): Mono<Long>
}