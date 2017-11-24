/*
 * Copyright (c) 2017 by Todd Ginsberg
 */

package com.ginsberg.counter.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.nio.ByteBuffer


@Repository
class RedisCounterRepository(private val redisTemplate: ReactiveRedisTemplate<String, String>,
                             @Value("\${redis.counter.key:THE_COUNTER}") keyName: String) : CounterRepository {

    private val key = ByteBuffer.wrap(StringRedisSerializer().serialize(keyName))

    override fun up(): Mono<Long> =
        redisTemplate.createMono { it.numberCommands().incr(key) }

    override fun down(): Mono<Long> =
        redisTemplate.createMono { it.numberCommands().decr(key) }

    override fun get(): Mono<Long> =
        redisTemplate.createMono { it.numberCommands().incrBy(key, 0L) }
}

