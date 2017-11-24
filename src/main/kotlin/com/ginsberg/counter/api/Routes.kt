/*
 * Copyright (c) 2017 by Todd Ginsberg
 */

package com.ginsberg.counter.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class Routes(private val counterHandler: CounterHandler) {

    @Bean
    fun counterRouter() = router {
        "/api/counter".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/", counterHandler::get)
                PUT("/up", counterHandler::up)
                PUT("/down", counterHandler::down)
            }
            accept(MediaType.TEXT_EVENT_STREAM).nest {
                GET("/", counterHandler::stream)
            }
        }
    }
}