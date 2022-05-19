package net.laohui.api.bean;

import lombok.Data;

@Data
public class UserRequestBody {
    private String username;
    private String password;
    private String code;
}
