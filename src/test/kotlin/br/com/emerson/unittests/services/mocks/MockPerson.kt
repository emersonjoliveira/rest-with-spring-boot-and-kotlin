package br.com.emerson.unittests.services.mocks

import br.com.emerson.model.Person

class MockPerson {

    fun mockEntityList(): ArrayList<Person> {
        val persons: ArrayList<Person> = ArrayList()
        for (i in 0..13) {
            persons.add(mockEntity(i))
        }
        return persons
    }

    fun mockEntity(number: Int): Person {
        val person = Person()
        person.address = "Address Test$number"
        person.firstName = "First Name Test$number"
        person.gender = if (number % 2 == 0) "Male" else "Female"
        person.id = number.toLong()
        person.lastName = "Last Name Test$number"
        return person
    }
}