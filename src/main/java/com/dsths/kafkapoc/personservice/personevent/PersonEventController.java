package com.dsths.kafkapoc.personservice.personevent;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsths.kafkapoc.personservice.person.PersonRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/events")
public class PersonEventController {

	private final PersonRepository personRepository;
	private final PersonEventSender personEventSender;

	public PersonEventController(PersonRepository personRepository, PersonEventSender personEventSender) {
		this.personRepository = personRepository;
		this.personEventSender = personEventSender;
	}   

	// TODO - this should be a post
	// send a person changed event for the person id {id}
	@GetMapping("/person/changed/{id}")
	public void sendPersonChangedEvent(@PathVariable("id") Long personId) {
		log.info("PersonEventController: sendPersonChangedEvent for: " + personId);
		personEventSender.sendEventViaAPICall(PersonEventType.CHANGED, personId);
	}

	// send a person changed event for all person records in the database:
	@GetMapping("/person/changed/all")
	public void resendPersonChangedEventsForAllPersons() {
		log.info("PersonEventController: resendPersonChangedEventsForAllPersons");

		personRepository.findAll().forEach((person) -> {
			personEventSender.sendEventViaAPICall(PersonEventType.CHANGED, person.getId());
		});
	}
}
