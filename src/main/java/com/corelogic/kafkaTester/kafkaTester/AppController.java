package com.corelogic.kafkaTester.kafkaTester;

import com.corelogic.kafkaTester.kafkaTester.service.IndexingService;
import com.corelogic.kafkaTester.kafkaTester.service.Producer;
import com.google.common.geometry.S2;
import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2CellId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@RestController
public class AppController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);
    private final Producer producer;
    private final IndexingService indexingService;

    @Autowired
    AppController(Producer producer, IndexingService indexingService) {
        this.producer = producer;
        this.indexingService = indexingService;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestBody String message) {
        this.producer.sendMessage(message);
    }

    @PostMapping(value = "/squish-unsquish")
    public void squishUnsquishMessage(@RequestBody byte[] message) {
        logger.info("SQUISHING AND UNSQUISHING");
        try {
            long startTime = System.nanoTime();
            // Encode a String into bytes
            //byte[] input = message.getBytes(StandardCharsets.UTF_8);
            byte[] input = message;
            logger.info("Input size: " + input.length + " bytes/ " + input.length/1e+6 + " mb");

            // Compress the bytes
            byte[] output = new byte[message.length];
            Deflater compresser = new Deflater();
            compresser.setInput(input);
            compresser.finish();
            int compressedDataLength = compresser.deflate(output);
            compresser.end();
            logger.info("Compressed size: " + compressedDataLength + " bytes/ " + compressedDataLength/1e+6 + " mb");
            logger.info("Compression ratio: " + (float)compressedDataLength/input.length);

            // Decompress the bytes
            Inflater decompresser = new Inflater();
            decompresser.setInput(output, 0, compressedDataLength);
            byte[] result = new byte[message.length];
            int resultLength = decompresser.inflate(result);
            decompresser.end();

            // Decode the bytes into a String
            //String outputString = new String(result, 0, resultLength, StandardCharsets.UTF_8);
            long endTime = System.nanoTime();
            long roundTripMillis = (endTime - startTime) / 1000000;
            logger.info("Messages match: " + Arrays.equals(result, input));
            logger.info("Sizes match: " + (resultLength == input.length));
            logger.info("Round trip time: " + roundTripMillis + " ms\n");

            int diffBytes = 0;
            for(int i = 0; i < result.length; i++){
                if(result[i] != input[i]){
                    diffBytes++;
                }
            }
            logger.info("# of different bytes: " + diffBytes);

        } catch (java.util.zip.DataFormatException ex) {
            // handle
        }
    }

    @PostMapping(value = "/squish-and-push")
    public void squishAndPushMessageToKafkaTopic(@RequestBody byte[] message) {
        logger.info("SQUISHING AND PUSHING");
        try {
            //long startTime = System.nanoTime();
            // Encode a String into bytes
            //byte[] input = message.getBytes(StandardCharsets.UTF_8);
            byte[] input = message;
            logger.info("Input length: " + input.length + " bytes/ " + input.length/1e+6 + " mb");

            // Compress the bytes
            byte[] output = new byte[(int) 1e+6];
            Deflater compresser = new Deflater();
            compresser.setInput(input);
            compresser.finish();
            int compressedDataLength = compresser.deflate(output);
            compresser.end();
            logger.info("Compressed length: " + compressedDataLength + " bytes/ " + compressedDataLength/1e+6 + " mb");
            logger.info("Compression ratio: " + (float)compressedDataLength/input.length);

            this.producer.sendMessage(output);
        } catch (Exception ex) {
            // handle
            logger.info(ex.getMessage());
        }
    }

    @PostMapping(value = "/s2-index")
    public ArrayList<String> indexWKB(@RequestBody String hexPolygon){
        ArrayList<String> ids = new ArrayList<>();
        byte[] wkbPolygon = HexFormat.of().parseHex(hexPolygon);
        try{
            ids.addAll(indexingService.coverPoly(wkbPolygon));
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return ids;
    }
}
