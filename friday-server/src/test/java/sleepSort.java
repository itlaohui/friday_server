import net.laohui.pojo.User;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class sleepSort {
    public static void main(String[] args) {
        UserProfile userProfile = new UserProfile();
        System.out.println("map=" + userProfile.asMap());
    }
}


class UserProfile {
    private String userName;
    private String nickName;
    private String phoneNumber;
    private String email;
    private Date loginTime;
    private String avatar;

    public UserProfile(User user) {
        this.userName = user.getUser_account_name();
        this.nickName = user.getUser_nickname();
        this.phoneNumber = user.getUser_phone_number();
        this.email = user.getUser_email();
        this.loginTime = user.getLogin_time();
    }

    public UserProfile() {
    }

    public Map<String, Object> asMap() {
        Class<? extends UserProfile> cls = this.getClass();
        Field[] declaredFields = cls.getDeclaredFields();
        Map<String, Object> map = new HashMap<>();
        for (Field field : declaredFields) {
            try {
                Field fieldAsName = cls.getDeclaredField(field.getName());
                fieldAsName.setAccessible(true);
                Object o = fieldAsName.get(this);
                map.put(field.getName(), o);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                System.out.println("error");
                continue;
            }
        }
        return map;
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

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
