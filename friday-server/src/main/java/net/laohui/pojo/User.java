package net.laohui.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Iterator;

@Data
@TableName("f_users")
public class User implements Iterable<User> {
//    @JSONField(serialize = false)
    @TableId(type = IdType.AUTO)
    private Integer user_id;
    private String user_account_name;
    @JSONField(serialize = false)
    private String user_account_password;
    private String user_nickname;
    private String user_phone_number;
    private String user_avatar;
    private String user_email;
    private Integer user_sex;
    private String user_status;
    private String user_role;
    private String login_ip;
    private String login_city;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date login_time;
    private String login_browser_ua;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date create_time;
    private String create_founder;
    private String create_ip;
    private String create_city;


    public int size() {
        Class<? extends User> aClass = this.getClass();
        return aClass.getFields().length;
    }

    @Override
    public Iterator<User> iterator() {
        return new ReverseIterator(this.size());
    }

    static class ReverseIterator implements Iterator<User> {
        int index;

        ReverseIterator(int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index > 0;
        }

        @Override
        public User next() {
            index--;
            return new User();
        }
    }
}
