package com.dsths.kafkapoc.personservice.personevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonEventSender {

    private final String personEventTopic;
    
    @Autowired
    private PersonEventRepository personEventRepository;
    

    private final KafkaTemplate<Long, PersonEventMessage> kafkaTemplate;

	public PersonEventSender(String personEventTopic, 
			KafkaTemplate<Long, PersonEventMessage> kafkaTemplate) {
		
		this.kafkaTemplate = kafkaTemplate;
		this.personEventTopic = personEventTopic; 
	}

	public void sendEventViaDataChange(PersonEvent personEvent) {
		// in this case the local event table record will have already been added.
	}
	
	@Async
	public void sendEventViaAPICall(PersonEventType eventType, Long personId) {

		// in this case the local event table record will not have already been added.
		
		PersonEvent personEvent = new PersonEvent(eventType, personId);
		personEvent.setEventStatus(EventStatus.SENDING);
		
		log.info("PersonEventSource - sending event: " + personEvent);

		// write to local event table as unsent:
		final PersonEvent savedPersonEvent = personEventRepository.save(personEvent);
		
		//send using personId as the key so that all events for the same person will go to the same partition to have guaranteed ordering:
		ListenableFuture<SendResult<Long, PersonEventMessage>>  future = kafkaTemplate.send(personEventTopic, personId, personEvent.getEventMessage());
		future.addCallback(new ListenableFutureCallback<SendResult<Long, PersonEventMessage>>(){

			@Override
			public void onSuccess(SendResult<Long, PersonEventMessage> result) {
				// update local event table to sent.
				savedPersonEvent.setEventStatus(EventStatus.SENT);
				personEventRepository.save(savedPersonEvent);
			}

			@Override
			public void onFailure(Throwable ex) {
				savedPersonEvent.setEventStatus(EventStatus.SEND_FAILED);
				savedPersonEvent.setSendFailureException(ex.getMessage());
				personEventRepository.save(savedPersonEvent);
				
			}
			
		});
		
		log.info("PersonEventSource - sending event finished" + personEvent);
	}
	
	public void resendEventsThatFailed() {
		// resend all the events that are in status SEND_FAILED
	}
	
}
