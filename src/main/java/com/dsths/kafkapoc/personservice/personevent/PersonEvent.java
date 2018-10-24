package com.dsths.kafkapoc.personservice.personevent;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class PersonEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Enumerated(EnumType.STRING)
	private PersonEventType eventType;

	private Long personId;

	@Enumerated(EnumType.STRING)
	private EventStatus EventStatus;

	private String sendFailureException;
	
	private final LocalDateTime createdTimestamp = LocalDateTime.now();

	public PersonEvent() {};
	
	public PersonEvent(PersonEventType eventType, Long personId) {
		this.eventType = eventType;
		this.personId = personId;
	}

	PersonEventMessage getEventMessage() {
		return new PersonEventMessage(getEventType(), getPersonId());
//		return new PersonEventMessage(getEventType(), getPersonId(), getCreatedTimestamp());
	}

}

enum EventStatus {
	NOT_SENT, SENDING, SEND_FAILED, SENT;
}
