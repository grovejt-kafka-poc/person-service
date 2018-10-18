package com.dsths.kafkatpoc.personservice.person;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonEventSender {

    private String personEventTopic;

    private final KafkaTemplate<String, PersonEvent> kafkaTemplate;

	public PersonEventSender(@Value("${kafka.topic.personEvent}") String personEventTopic, 
			KafkaTemplate<String, PersonEvent> kafkaTemplate) {
		
		this.kafkaTemplate = kafkaTemplate;
		this.personEventTopic = personEventTopic; 
	}

	public void sendEvent(PersonEventType eventType, Long personId) {


		PersonEvent personEvent = new PersonEvent(eventType, personId);
		
		log.info("PersonEventSource - sending event: " + personEvent);

		//send using personId as the key so that all events for the same person will go to the same partition to have guaranteed ordering:
		kafkaTemplate.send(personEventTopic, Long.toString(personId), personEvent); 
//		kafkaTemplate.send(personEventTopic, Long.toString(personId), "this will be a person event for personId: " + personId); 
	}
}
