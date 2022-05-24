package net.laohui.api.bean;

import net.laohui.api.util.ClassToMap;
import java.util.Map;

//@Log4j2
//@Getter
//@Setter
//@ToString
public class UserProfile {
    private String userName;
    private String nickName;
    private String phoneNumber;
    private String email;
    private Long loginTime;
    private String avatar;

    public UserProfile(User user) {
        this.userName = user.getUser_account_name();
        this.nickName = user.getUser_nickname();
        this.phoneNumber = user.getUser_phone_number();
        this.avatar = user.getUser_avatar();
        this.email = user.getUser_email();
        this.loginTime = user.getLogin_time().getTime();
    }

    public UserProfile() {
    }

    public Map<String, Object> asMap() {
        return ClassToMap.asMap(this);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", loginTime=" + loginTime +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
