package com.ygn.springboot;

import io.minio.ListObjectsArgs;
import io.minio.Result;
import io.minio.messages.Item;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class kafkaTest {

    @Test
    public  void  testProducer() throws  Exception {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer .class.getName());
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String,String> kafkaProducer = new KafkaProducer<String,String>(configMap);

//        ProducerRecord<String,String> producerRecord = new ProducerRecord<String,String>("test",
//                "key","value");
//        kafkaProducer.send(producerRecord);
        for (int i = 0; i < 10; i++) {
        ProducerRecord<String,String> producerRecord = new ProducerRecord<String,String>("test",
                "key"+i,"value"+i);
        kafkaProducer.send(producerRecord);
        }
        kafkaProducer.close();
    }


    @Test
    public  void  testConsumer() throws  Exception {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, "ygn");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(configMap);

        kafkaConsumer.subscribe(Collections.singletonList("test"));
        while (true){
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);
            for ( ConsumerRecord<String, String> data: consumerRecords ){
                System.out.println(data);
            }
        }
//        kafkaConsumer.close();

    }


}
