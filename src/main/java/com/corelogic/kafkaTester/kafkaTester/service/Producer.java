package com.corelogic.kafkaTester.kafkaTester.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "spatial_us-wvsplatform-kafka-to-bq-spike";

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//    @Autowired
//    private KafkaTemplate<String, byte[]> kafkaByteTemplate;

    public void sendMessage(String message) {
        logger.info(String.format("#### -> Producing message -> %s", message));
        //this.kafkaTemplate.send(TOPIC, message);
    }
    public void sendMessage(byte[] message) {
        logger.info(String.format("#### -> Producing message -> %s", message));
        //this.kafkaByteTemplate.send(TOPIC, message);
    }
}
