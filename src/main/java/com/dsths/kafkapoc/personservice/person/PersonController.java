package com.dsths.kafkapoc.personservice.person;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/persons")
public class PersonController {

	@Autowired
	private PersonRepository personRepository;

	@GetMapping()
	public ResponseEntity<List<Person>> findAllPersons() {
		log.info("PersonController: getPersons");
		return new ResponseEntity<List<Person>>(personRepository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{personId}")
	public ResponseEntity<Person> findById(@PathVariable long personId) {
		log.info("PersonController: findById - " + personId);
		
		Optional<Person> person = personRepository.findById(personId);
		if (!person.isPresent()) {
			throw new PersonNotFoundException("id:" + personId);
		}
		
		return new ResponseEntity<Person>(person.get(), HttpStatus.OK);
	}
	
	@GetMapping("/search/findByLastName")
	public List<Person> findByLastName(@RequestParam("lastName") String lastName) {
		log.info("PersonController: findByLastName - " + lastName);
		return personRepository.findByLastName(lastName);
	}


}
