package com.dsths.kafkatpoc.personservice.person;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PersonEvent {

	private final PersonEventType eventType;
	private final Long personId;
//	private final LocalDateTime timestamp = LocalDateTime.now();
	
	
	public PersonEvent(PersonEventType eventType, Long personId) {
		this.eventType = eventType;
		this.personId = personId;
	}


}
enum PersonEventType {
	CREATED, CHANGED, DELETED;
}
