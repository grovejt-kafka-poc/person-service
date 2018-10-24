package com.dsths.kafkapoc.personservice.personevent;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync  //needed to allow the producer send methods to be async
@Configuration
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;
    
    
    @Value("${kafka.topic.personEvent}") 
	private String topic;
    
    
	@Bean
	public Map<String, Object> producerConfigs() {

		Map<String, Object> configProps = new HashMap<>();
		
		// list of host:port pairs used for establishing the initial connections to the Kakfa cluster:
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		
		// timeout in x seconds for testing:
		configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 2000);
//		configProps.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 3000);
//		configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 4000);
		return configProps;
	}
	
	@Bean
	public ProducerFactory<Long, PersonEventMessage> producerFactory() {

		return new DefaultKafkaProducerFactory<Long, PersonEventMessage>(producerConfigs());
	}
    
	@Bean
	public KafkaTemplate<Long, PersonEventMessage> kafkaTemplate() {
		KafkaTemplate<Long, PersonEventMessage> kafkaTemplate = new KafkaTemplate<>(producerFactory());
		return kafkaTemplate;
	}
	

	
	@Bean
	public PersonEventSender personEventSender() {
	  return new PersonEventSender(topic, kafkaTemplate());
	}
}
