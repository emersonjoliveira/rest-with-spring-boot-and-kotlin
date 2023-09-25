package br.com.emerson.unittests.services

import br.com.emerson.exception.RequiredObjectIsNullException
import br.com.emerson.model.Person
import br.com.emerson.repository.PersonRepository
import br.com.emerson.services.PersonServices
import br.com.emerson.unittests.services.mocks.MockPerson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
class PersonServicesTest {

    private var input: MockPerson? = null

    @InjectMocks
    private val service: PersonServices? = null

    @Spy
    private val repository: PersonRepository? = null

    @BeforeEach
    fun setupMock() {
        input = MockPerson()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testFindById() {
        val person = input!!.mockEntity(1)
        `when`(repository?.findById(1)).thenReturn(Optional.of(person))

        val result = service!!.findById(1)

        assertThat(result).isNotNull
        assertThat(result.id).isNotNull
        assertThat(result.address).isEqualTo("Address Test1")
        assertThat(result.firstName).isEqualTo("First Name Test1")
        assertThat(result.lastName).isEqualTo("Last Name Test1")
        assertThat(result.gender).isEqualTo("Female")
    }

    @Test
    fun testFindAll() {
        val list : List<Person> = input!!.mockEntityList()
        `when`(repository?.findAll()).thenReturn(list)

        val persons = service!!.findAll()

        assertThat(persons).isNotNull.hasSize(14)

        val personOne = persons[1]

        assertThat(personOne).isNotNull
        assertThat(personOne.id).isNotNull
        assertThat(personOne.address).isEqualTo("Address Test1")
        assertThat(personOne.firstName).isEqualTo("First Name Test1")
        assertThat(personOne.lastName).isEqualTo("Last Name Test1")
        assertThat(personOne.gender).isEqualTo("Female")

        val personFour = persons[4]

        assertThat(personFour).isNotNull
        assertThat(personFour.id).isNotNull
        assertThat(personFour.address).isEqualTo("Address Test4")
        assertThat(personFour.firstName).isEqualTo("First Name Test4")
        assertThat(personFour.lastName).isEqualTo("Last Name Test4")
        assertThat(personFour.gender).isEqualTo("Male")
    }

    @Test
    fun testCreate() {
        val entity = input!!.mockEntity(1)
        val persisted = entity.copy()
        entity.id = null

        `when`(repository?.save(entity)).thenReturn(persisted)

        val result = service!!.create(entity)

        assertThat(result).isNotNull
        assertThat(result.id).isNotNull
        assertThat(result.address).isEqualTo("Address Test1")
        assertThat(result.firstName).isEqualTo("First Name Test1")
        assertThat(result.lastName).isEqualTo("Last Name Test1")
        assertThat(result.gender).isEqualTo("Female")
    }

    @Test
    fun testCreateWithNullPerson() {
        val exception: Exception = assertThrows<RequiredObjectIsNullException> {
            service!!.create(null)
            throw RequiredObjectIsNullException()
        }
        val expectedMessage = "It's not allowed to persist a null object"
        assertThat(exception.message).isEqualTo(expectedMessage)
    }

    @Test
    fun testUpdate() {
        val entity = input!!.mockEntity(1)
        val persisted = entity.copy()

        `when`(repository?.findById(1)).thenReturn(Optional.of(entity))
        `when`(repository?.save(entity)).thenReturn(persisted)

        val result = service!!.create(entity)

        assertThat(result).isNotNull
        assertThat(result.id).isNotNull
        assertThat(result.address).isEqualTo("Address Test1")
        assertThat(result.firstName).isEqualTo("First Name Test1")
        assertThat(result.lastName).isEqualTo("Last Name Test1")
        assertThat(result.gender).isEqualTo("Female")
    }

    @Test
    fun testUpdateWithNullPerson() {
        val exception: Exception = assertThrows<RequiredObjectIsNullException> {
            service!!.update(null)
            throw RequiredObjectIsNullException()
        }
        val expectedMessage = "It's not allowed to persist a null object"
        assertThat(exception.message).isEqualTo(expectedMessage)
    }

    @Test
    fun testDelete() {
        val person = input!!.mockEntity(1)
        `when`(repository?.findById(1)).thenReturn(Optional.of(person))

        service!!.delete(1)
    }
}