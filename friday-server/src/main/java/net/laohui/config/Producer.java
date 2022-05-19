package net.laohui.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.laohui.api.bean.KafkaMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Producer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMsg(String topic, String msg) {
        System.out.println("发送消息内容 :" + msg);
        ObjectMapper mapper = new ObjectMapper();
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setKey("key");
        kafkaMessage.setValue(msg);
        kafkaMessage.setTopic("topic");
        String json = "";
        try {
            json = mapper.writeValueAsString(kafkaMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send(topic, json);
    }

}