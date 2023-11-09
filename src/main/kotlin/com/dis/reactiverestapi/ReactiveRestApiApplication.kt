package com.dis.reactiverestapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ReactiveRestApiApplication

fun main(args: Array<String>) {
    runApplication<ReactiveRestApiApplication>(*args)
}
