package br.com.emerson.repository

import br.com.emerson.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<Person, Long?>