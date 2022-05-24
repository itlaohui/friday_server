package net.laohui.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import net.laohui.api.bean.KafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "remote_mail")
    public void receiveMsg(String s) throws InterruptedException {
        System.out.println("start");
        System.out.println("处理消息 : "+ s);
        Thread.sleep(5000);
        System.out.println("end");
    }

    @KafkaListener(topics = "remote_sex")
    public void receiveMsg(ConsumerRecord<?, ?> record) throws InterruptedException {
        System.out.println("start");
        System.out.println("处理消息 : "+ record);
        Optional.ofNullable(record.value())
                .ifPresent(message -> {
                    if (record.value() instanceof ObjectMapper) {
                        ObjectMapper mapper = (ObjectMapper) record.value();
                        KafkaMessage kafkaMessage = new KafkaMessage();
                        try {
                            mapper.writeValueAsString(kafkaMessage);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        log.info("消息内容 : {}", kafkaMessage);
                    }
                    log.info("【+++++++++++++++++ record = {} 】", record.value());
                    log.info("【+++++++++++++++++ message = {}】", message);
                });
        System.out.println("end");
    }

//    @Scheduled(fixedRate = 5000)
//    public void receiveMsg2() throws InterruptedException {
//        log.warn("监听消息中...");
//    }
}
