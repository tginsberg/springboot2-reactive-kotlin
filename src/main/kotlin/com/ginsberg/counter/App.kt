/*
 * Copyright (c) 2017 by Todd Ginsberg
 */

package com.ginsberg.counter

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class App

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}
