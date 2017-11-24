/*
 * Copyright (c) 2017 by Todd Ginsberg
 */

package com.ginsberg.counter.api

import com.ginsberg.counter.model.CounterState
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.test.StepVerifier
import java.time.LocalDateTime


@RunWith(SpringRunner::class)
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT
)
class CounterRoutesTest {

    lateinit var webClient: WebTestClient

    @Autowired
    lateinit var route: RouterFunction<ServerResponse>

    lateinit var state: CounterState

    @Before
    fun before() {
        webClient = WebTestClient.bindToRouterFunction(route).build()

        state = webClient.get()
            .uri("/api/counter")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody(CounterState::class.java)
            .returnResult().responseBody!!
    }

    @Test
    fun `get current value`() {
        assertThat(state.value).isNotNull()
        assertThat(state.asOf)
            .isNotNull()
            .isAfterOrEqualTo(LocalDateTime.now().minusSeconds(1))
            .isBeforeOrEqualTo(LocalDateTime.now())
    }

    @Test
    fun `increment counter`() {
        val event: String? = webClient.put()
            .uri("/api/counter/up")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertThat(event).isNotNull()
        assertThat(JsonPath.read<Long>(event, "$.value", null)).isEqualTo(state.value.inc())
    }

    @Test
    fun `decrement counter`() {
        val event: String? = webClient.put()
            .uri("/api/counter/down")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertThat(event).isNotNull()
        assertThat(JsonPath.read<Long>(event, "$.value", null)).isEqualTo(state.value.dec())

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD) // Because of ReplayProcessor state.
    fun `stream receives events`() {

        // Send in an event, so the stream has something to emit or the next part
        // has trouble when using WebTestClient with this test in isolation for some reason.
        webClient.put().uri("/api/counter/up").exchange()

        val events = webClient.get()
            .uri("/api/counter")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM)
            .returnResult(String::class.java)
            .responseBody

        StepVerifier.create(events)
            .assertNext {
                assertThat(JsonPath.read<String>(it, "$.type", null)).isEqualToIgnoringCase("up")
                assertThat(JsonPath.read<Long>(it, "$.value", null)).isEqualTo(1L)
            }
            .then {
                webClient.put().uri("/api/counter/up").exchange()
            }
            .assertNext {
                assertThat(JsonPath.read<String>(it, "$.type", null)).isEqualToIgnoringCase("up")
                assertThat(JsonPath.read<Long>(it, "$.value", null)).isEqualTo(2L)
            }
            .then {
                webClient.put().uri("/api/counter/down").exchange()
            }
            .assertNext {
                assertThat(JsonPath.read<String>(it, "$.type", null)).isEqualToIgnoringCase("down")
                assertThat(JsonPath.read<Long>(it, "$.value", null)).isEqualTo(1L)
            }
            .thenCancel()
            .verify()
    }

}