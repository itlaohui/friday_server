package net.laohui.pojo;

import lombok.Data;

@Data
public class UserRequestBody {
    private String username;
    private String password;
    private String code;
}
