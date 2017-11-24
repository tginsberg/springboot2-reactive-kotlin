/*
 * Copyright (c) 2017 by Todd Ginsberg
 */

package com.ginsberg.counter.model

import java.time.LocalDateTime

data class CounterState(val value: Long,
                        val asOf: LocalDateTime = LocalDateTime.now())

sealed class CounterEvent(val type: String,
                          val at: LocalDateTime = LocalDateTime.now())

data class CounterUp(val value: Long) : CounterEvent("up")
data class CounterDown(val value: Long) : CounterEvent("down")

