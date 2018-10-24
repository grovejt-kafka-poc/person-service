package com.dsths.kafkapoc.personservice.personevent;

import static org.junit.Assert.assertThat;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasKey;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

/**
 * See https://www.codenotfound.com/spring-kafka-embedded-unit-test-example.html
 * See https://blog.mimacom.com/testing-apache-kafka-with-spring-boot/
 * 
 * In this test case we will be testing the Sender by sending a message to the
 * topic. We will verify whether the sending works by setting up a test-listener
 * on the topic. All of the setup will be done before the test case runs using
 * the @Before annotation.
 * 
 * For creating the needed consumer properties a static consumerProps() method
 * provided by KafkaUtils is used. We then create a DefaultKafkaConsumerFactory
 * and ContainerProperties which contains runtime properties (in this case the
 * topic name) for the listener container. Both are then passed to the
 * KafkaMessageListenerContainer constructor.
 * 
 * Received messages need to be stored somewhere. In this example, a thread safe
 * BlockingQueue is used. We create a new MessageListener and in the onMessage()
 * method we add the received message to the BlockingQueue.
 * 
 * The listener is started by starting the container.
 * 
 * In order to avoid that we send a message before the container has required
 * the number of assigned partitions, we use the waitForAssignment() method on
 * the ContainerTestUtils helper class.
 * 
 * The actual unit test itself consists out dummy person changed events and
 * asserting that the received value is the same as the one that was sent.
 * 
 * The Spring Kafka Test JAR ships with a number of Hamcrest Matchers that allow
 * checking if the key, value or partition of a received message matches with an
 * expected value. In the below unit test we use a Matcher to check the value of
 * the received message.
 * 
 * The JAR also includes some AssertJ conditions that allow asserting if a
 * received message contains a specific key, value or partition. We illustrate
 * the usage of such a condition by asserting that the key of the received
 * message is null.
 * 
 * For both the Hamcrest matchers and AssertJ conditions, make sure the static
 * imports have been specified.
 * 
 * Note the @DirtiesContext annotation that ensures the correct Kafka broker
 * address is set as explained above.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(topics = "${kafka.topic.personEvent}", count = 1, controlledShutdown = true)
@Slf4j
public class PersonEventSenderTest {

	@Autowired
	private KafkaEmbedded embeddedKafka;

	@Value("${kafka.topic.personEvent}")
	private String topic;

	@Value("${spring.embedded.kafka.brokers}")
	private String brokerAddresses;

	@Autowired
	private PersonEventSender personEventSender;

	private KafkaMessageListenerContainer<Long, PersonEvent> container;
	private BlockingQueue<ConsumerRecord<Long, PersonEvent>> records;

	@Before
	public void setUp() throws Exception {

		// set up the Kafka consumer properties
		Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("testGroupId", "false", embeddedKafka);
		
		// create a Kafka consumer factory
		JsonDeserializer<PersonEvent> personEventJsonDeserializer = new JsonDeserializer<>(PersonEvent.class);
		personEventJsonDeserializer.addTrustedPackages("*");
		
		DefaultKafkaConsumerFactory<Long, PersonEvent> consumerFactory = new DefaultKafkaConsumerFactory<Long, PersonEvent>(consumerProperties,
				new LongDeserializer(), personEventJsonDeserializer);

		// set the topic that needs to be consumed
		ContainerProperties containerProperties = new ContainerProperties(topic);

		// create a Kafka MessageListenerContainer
		container = new KafkaMessageListenerContainer<Long, PersonEvent>(consumerFactory, containerProperties);

		// create a thread safe queue to store the received message
		records = new LinkedBlockingQueue<>();

		// setup a Kafka message listener
		container.setupMessageListener(new MessageListener<Long, PersonEvent>() {
			@Override
			public void onMessage(ConsumerRecord<Long, PersonEvent> record) {
				log.debug("test-listener received message='{}'", record.toString());
				records.add(record);
			}
		});

		// start the container and underlying message listener
		container.start();

		// wait until the container has the required number of assigned partitions
		ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
	}

	@After
	public void tearDown() {
		// stop the container
		container.stop();
	}

	@Test
	public void testSend() throws InterruptedException {

		PersonEvent expectedPersonEvent = new PersonEvent(PersonEventType.CHANGED, 1L);
		
		// send the Event
		personEventSender.sendEventViaAPICall(PersonEventType.CHANGED, 1L);

		// check that the event was received
		ConsumerRecord<Long, PersonEvent> received = records.poll(10, TimeUnit.SECONDS);
		
		// Hamcrest Matchers to check the value
		assertThat(received, hasValue(expectedPersonEvent));
		assertThat(received, hasKey(1L));
	}

}
