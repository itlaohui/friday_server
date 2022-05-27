package net.laohui.api.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ConfigExt implements Serializable {
    private Integer id;
    private Integer cid;
    private String key;
    private String value;
    private String title;
    private Date create_time;
}
