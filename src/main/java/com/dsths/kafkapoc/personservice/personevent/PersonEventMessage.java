package com.dsths.kafkapoc.personservice.personevent;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * This is the message for the person event that will be sent to kafka.
 * @author grove
 *
 */
@Data
public class PersonEventMessage {

	private final PersonEventType eventType;

	private final Long personId;

//	private final LocalDateTime createdTimestamp;

	public PersonEventMessage(PersonEventType eventType, Long personId) {
		this.eventType = eventType;
		this.personId = personId;
	}
//	public PersonEventMessage(PersonEventType eventType, Long personId, LocalDateTime createdTimestamp) {
//		this.eventType = eventType;
//		this.personId = personId;
//		this.createdTimestamp = createdTimestamp;
//	}
}

