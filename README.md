# person-service
kafka-poc person-service : Spring Boot with Spring Data Rest service to manage people in the Person database and send PersonEvents to Kafka

Whenever a person is added, changed, or deleted a corresponding personEvent will be created by a jpa listener and saved within the same transaction to the local event table as 'sending'
 
A person changed event can also be created using a REST call to http://{hostname:port}/events/person/changed/{personId}
Person changed events for all persons in the database can be created with a REST call to:  http://{hostname:port}/events/person/changed/all

When a person event is created it is then saved to the person_event table with a status of 'not sent' (or perhaps 'sending'?).  This is done in case the connection to
kafka is down and the event can't be sent.

Then the event is sent to kafka and if it was successfully sent the person_event table record for that event is updated with a status of sent.

## H2-Console
* http://localhost:8010/h2
* sa/'blank'

## REST API:

### Persons
* To see all persons in the database:
   * GET /api/persons
   * http://localhost:8010/api/persons
* To see a single person in the database:
   * GET /api/persons/{personId}  
   * http://localhost:8010/api/persons/{3} 
* To find a all persons by last name:
   * GET /api/search/findByLastName
   * http://localhost:8010/api/search/persons/findByLastName?lastName=LastName-2    
   
## Swagger UI API Documentation: 
* http://localhost:8010/swagger-ui.html


## Scenarios

Scenario 1:
Person record is changed
....

Scenario 1:
A person is created through the mab-person-service.
  Post call made to mab-person-service to create a new person.
  Inside the transaction to create the new person the event to be sent is saved with event type of CREATED and a status of NOT_SENT
  The the event message for the event is sent and the status is changed to SENT 



Scenario 2:
api call made to send event for specific person (specify event type and person id)


Scenario 3:
api call made to send events for all persons (specify event type)


Error Scenario
A person is created without an email address, (email is required in AA)

Persons from the person table in the Persons database are replicated to become Members in the members/addresses tables in the Members database.
