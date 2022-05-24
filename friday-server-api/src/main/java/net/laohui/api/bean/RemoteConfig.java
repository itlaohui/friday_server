package net.laohui.api.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@TableName(value = "f_remote_config")
public class RemoteConfig implements Serializable {
    @Id
    private String id;
    private String config_key;
    private String config_type;
    private String config_value;
    private String belong_application;
    private boolean need_login;
    private String create_time;
    private String create_founder;



}
