package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@Testcontainers
class KafkaApplicationTests {

	companion object {
		@JvmStatic
		@Container
		val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
	}

	@Test
	fun contextLoads() {
	}

}
