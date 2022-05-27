package net.laohui.api.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRequestBody implements Serializable {
    private String username;
    private String password;
//    private String code;
}
