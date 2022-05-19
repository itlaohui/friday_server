package net.laohui.enumerate;

import net.laohui.enumerate.enuminterface.UserLevelEnumInterface;

public enum UserLevelEnum implements UserLevelEnumInterface {
    ROLE_ROOT_ADMIN(9002, "超级管理员"){
        @Override
        public boolean allowAdmin() {
            return true;
        }

        @Override
        public boolean allowSystem() {
            return true;
        }

        @Override
        public boolean allowUser() {
            return true;
        }
    },

    ROLE_NORMAL_ADMIN(9001, "管理员"){
        @Override
        public boolean allowAdmin() {
            return true;
        }

        @Override
        public boolean allowSystem() {
            return false;
        }

        @Override
        public boolean allowUser() {
            return true;
        }
    },

    ROLE_NORMAL(9000, "普通用户"){
        @Override
        public boolean allowAdmin() {
            return false;
        }

        @Override
        public boolean allowSystem() {
            return false;
        }

        @Override
        public boolean allowUser() {
            return true;
        }
    };

    private Integer code;
    private String message;

    UserLevelEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
