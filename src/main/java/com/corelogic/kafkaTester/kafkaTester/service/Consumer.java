package com.corelogic.kafkaTester.kafkaTester.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.Inflater;

@Service
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    //@KafkaListener(topics = "spatial_us-wvsplatform-kafka-to-bq-spike", groupId = "spatial_us-wvsplatform-kafka-to-bq-spike")
    public void consume(byte[] output) throws IOException {
        try {
            // Decompress the bytes
            Inflater decompresser = new Inflater();
            decompresser.setInput(output, 0, output.length);
            byte[] result = new byte[(int) 5e+8];
            int resultLength = decompresser.inflate(result);
            decompresser.end();

            // Decode the bytes into a String
            String outputString = new String(result, 0, resultLength, StandardCharsets.UTF_8);
            logger.info(String.format("Consumed message size: %d", result.length));
        }
        catch (Exception ex){
            logger.info(ex.getMessage());
        }
    }
}
