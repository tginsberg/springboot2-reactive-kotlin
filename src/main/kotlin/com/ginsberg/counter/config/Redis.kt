/*
 * Copyright (c) 2017 by Todd Ginsberg
 */

package com.ginsberg.counter.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext


@Configuration
class Redis {

    @Bean
    fun template(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, String> =
        ReactiveRedisTemplate(factory, RedisSerializationContext.string())
}