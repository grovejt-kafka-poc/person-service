package com.dsths.kafkatpoc.personservice.person;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PersonEventController {

	private final PersonRepository personRepository;
	private final PersonEventSender personEventGenerator;

	public PersonEventController(PersonRepository personRepository, PersonEventSender personEventSource) {
		this.personRepository = personRepository;
		this.personEventGenerator = personEventSource;
	}

	// TODO - this should be a post
	// send a person changed event for the person id {id}
	@GetMapping("/events/person/changed/{id}")
	public void sendPersonChangedEvent(@PathVariable("id") Long personId) {
		log.info("PersonEventController: sendPersonChangedEvent for: " + personId);
		personEventGenerator.sendEvent(PersonEventType.CHANGED, personId);
	}

	// send a person changed event for all person records in the database:
	@GetMapping("/events/person/changed/all")
	public void resendPersonChangedEventsForAllPersons() {
		log.info("PersonEventController: resendPersonChangedEventsForAllPersons");

		personRepository.findAll().forEach((person) -> {
			personEventGenerator.sendEvent(PersonEventType.CHANGED, person.getId());
		});
	}
}
