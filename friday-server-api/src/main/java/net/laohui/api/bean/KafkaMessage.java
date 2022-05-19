package net.laohui.api.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class KafkaMessage implements Serializable {
    private String topic;
    private String key;
    private String value;
}