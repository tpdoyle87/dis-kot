package com.dis.reactiverestapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class ReactiveRestApiApplication

fun main(args: Array<String>) {
    runApplication<ReactiveRestApiApplication>(*args)
}
