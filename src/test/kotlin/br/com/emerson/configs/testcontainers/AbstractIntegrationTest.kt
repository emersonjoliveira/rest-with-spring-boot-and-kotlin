package br.com.emerson.configs.testcontainers

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.lifecycle.Startable
import org.testcontainers.lifecycle.Startables
import java.util.stream.Stream

@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
open class AbstractIntegrationTest {

    internal class Initializer: ApplicationContextInitializer<ConfigurableApplicationContext> {

        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            startContainers()
            val environment = applicationContext.environment
            val testcontainers = MapPropertySource("testcontainers", createConnectionConfiguration())
            environment.propertySources.addFirst(testcontainers)
        }

        companion object {

            private var mysql : MySQLContainer<*> = MySQLContainer("mysql:8.0.28")

            private fun createConnectionConfiguration(): MutableMap<String, Any> {
                return java.util.Map.of(
                    "sptring.datasource.url", mysql.jdbcUrl,
                    "sptring.datasource.username", mysql.username,
                    "sptring.datasource.password", mysql.password
                )
            }

            private fun startContainers() {
                Startables.deepStart(Stream.of(mysql)).join()
            }
        }
    }
}