package net.laohui.enumerate;

import net.laohui.enumerate.enuminterface.UserStatusEnumInterface;

public enum UserStatusEnum implements UserStatusEnumInterface {
    USER_STATUS_BAN(9003,"账户已被封禁！")  {
        @Override
        public boolean allowLogin() {
            return false;
        }
    },

    USER_STATUS_LOGOFF(9002,"该账户已被注销！") {
        @Override
        public boolean allowLogin() {
            return false;
        }
    },

    USER_STATUS_LOCKING(9001, "账户处于锁定状态！") {
        @Override
        public boolean allowLogin() {
            return false;
        }
    },

    USER_STATUS_NORMAL(9000,"账户正常！") {
        @Override
        public boolean allowLogin() {
            return true;
        }
    };

    private Integer code;
    private String message;

    UserStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
