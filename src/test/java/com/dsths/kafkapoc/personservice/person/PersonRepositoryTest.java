package com.dsths.kafkapoc.personservice.person;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class PersonRepositoryTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testGetPersons() {
		Person[] persons = restTemplate.getForObject("/api/persons", Person[].class);
		assertTrue(persons.length == 1000);
	}
	
	@Test
	public void testFindByLastName() {
		Person[] persons = restTemplate.getForObject("/api/persons/search/findByLastName?lastName=lastName-1", Person[].class);
		assertTrue(persons.length == 1);
		Person person = persons[0];
		assertEquals("firstName-1", person.getFirstName());
	}



}
