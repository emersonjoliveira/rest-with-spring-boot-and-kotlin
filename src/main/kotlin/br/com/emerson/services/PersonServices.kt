package br.com.emerson.services

import br.com.emerson.exception.RequiredObjectIsNullException
import br.com.emerson.exception.ResourceNotFoundException
import br.com.emerson.model.Person
import br.com.emerson.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonServices(private val repository: PersonRepository) {

    fun create(person: Person?): Person {
        if (person == null) {
            throw RequiredObjectIsNullException()
        }
        return repository.save(person)
    }

    fun findAll(): List<Person> = repository.findAll()

    fun findById(id: Long): Person = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }

    fun update(person: Person?): Person {
        if (person == null) {
            throw RequiredObjectIsNullException()
        }
        val entity: Person = repository.findById(person.id!!)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }
        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender
        return repository.save(entity)
    }

    fun delete(id: Long) {
        val entity: Person = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }
        repository.delete(entity)
    }
}