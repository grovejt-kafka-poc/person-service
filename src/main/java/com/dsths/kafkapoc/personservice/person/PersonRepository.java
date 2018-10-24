package com.dsths.kafkapoc.personservice.person;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface PersonRepository extends JpaRepository<Person, Long>{

	//e.g http://localhost:8010/persons/search/findByLastName?lastName=lastName-1
	List<Person> findByLastName(@Param("lastName") String lastName);
}
