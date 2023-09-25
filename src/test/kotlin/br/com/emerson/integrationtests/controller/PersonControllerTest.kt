package br.com.emerson.integrationtests.controller

import br.com.emerson.configs.TestsConfig
import br.com.emerson.model.Person
import br.com.emerson.testcontainers.AbstractIntegrationTest
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.common.mapper.TypeRef
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerTest : AbstractIntegrationTest() {

    private var specification : RequestSpecification? = null
    private var objectMapper : ObjectMapper? = null
    private var person : Person? = null

    @BeforeAll
    fun setup() {
        objectMapper = ObjectMapper()
        objectMapper!!.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        person = Person()
    }

    @Test
    @Order(1)
    fun postSetup() {
        specification = RequestSpecBuilder()
            .setBasePath("/person")
            .setPort(TestsConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(2)
    fun testCreate() {
        mockPerson()
        val content : String = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE)
            .body(person)
            .`when`().post().then()
            .statusCode(HttpStatus.OK.value())
            .extract().body().asString()
        val createdPerson = objectMapper!!.readValue(content, Person::class.java)

        person = createdPerson

        assertThat(createdPerson.id).isNotNull().isGreaterThan(0)
        assertThat(createdPerson.firstName).isNotNull().isEqualTo("Richard")
        assertThat(createdPerson.lastName).isNotNull().isEqualTo("Stallman")
        assertThat(createdPerson.address).isNotNull().isEqualTo("New York City, New York, US")
        assertThat(createdPerson.gender).isNotNull().isEqualTo("Male")
    }

    @Test
    @Order(3)
    fun testUpdate() {
        person!!.lastName = "Matthew Stallman"
        val content : String = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE)
            .body(person)
            .`when`().put().then()
            .statusCode(HttpStatus.OK.value())
            .extract().body().asString()
        val updatedPerson = objectMapper!!.readValue(content, Person::class.java)

        person = updatedPerson

        assertThat(updatedPerson.id).isNotNull().isEqualTo(10)
        assertThat(updatedPerson.firstName).isNotNull().isEqualTo("Richard")
        assertThat(updatedPerson.lastName).isNotNull().isEqualTo("Matthew Stallman")
        assertThat(updatedPerson.address).isNotNull().isEqualTo("New York City, New York, US")
        assertThat(updatedPerson.gender).isNotNull().isEqualTo("Male")
    }

    @Test
    @Order(4)
    fun testFindById() {
        val content : String = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE)
            .pathParam("id", person!!.id)
            .`when`().get("{id}").then()
            .statusCode(HttpStatus.OK.value())
            .extract().body().asString()
        val foundPerson = objectMapper!!.readValue(content, Person::class.java)

        person = foundPerson

        assertThat(foundPerson.id).isNotNull().isEqualTo(10)
        assertThat(foundPerson.firstName).isNotNull().isEqualTo("Richard")
        assertThat(foundPerson.lastName).isNotNull().isEqualTo("Matthew Stallman")
        assertThat(foundPerson.address).isNotNull().isEqualTo("New York City, New York, US")
        assertThat(foundPerson.gender).isNotNull().isEqualTo("Male")
    }

    @Test
    @Order(5)
    fun testDelete() {
        given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE)
            .pathParam("id", person!!.id)
            .`when`().delete("{id}").then()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }

    @Test
    @Order(6)
    fun testFindAll() {
        val content = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE)
            .`when`().get().then()
            .statusCode(HttpStatus.OK.value())
            .extract().body().`as`(object : TypeRef<List<Person?>?>(){})

        val foundPersonOne = content?.get(0)

        assertThat(foundPersonOne!!.id).isNotNull()
        assertThat(foundPersonOne.firstName).isNotNull().isEqualTo("Leandro")
        assertThat(foundPersonOne.lastName).isNotNull().isEqualTo("Costa")
        assertThat(foundPersonOne.address).isNotNull().isEqualTo("Uberlândia - Minas Gerais - Brasil")
        assertThat(foundPersonOne.gender).isNotNull().isEqualTo("Male")

        val foundPersonSix = content[5]

        assertThat(foundPersonSix!!.id).isNotNull()
        assertThat(foundPersonSix.firstName).isNotNull().isEqualTo("Marcos")
        assertThat(foundPersonSix.lastName).isNotNull().isEqualTo("Paulo")
        assertThat(foundPersonSix.address).isNotNull().isEqualTo("Patos de Minas - Minas Gerais - Brasil")
        assertThat(foundPersonSix.gender).isNotNull().isEqualTo("Male")
    }

    private fun mockPerson() {
        person?.firstName = "Richard"
        person?.lastName = "Stallman"
        person?.address = "New York City, New York, US"
        person?.gender = "Male"
    }
}