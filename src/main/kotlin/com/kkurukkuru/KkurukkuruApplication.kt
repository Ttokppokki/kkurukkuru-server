package com.kkurukkuru

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class KkurukkuruApplication

fun main(args: Array<String>) {
    runApplication<KkurukkuruApplication>(*args)
}
