package com.java.train.exception;

public enum BusinessExceptionEnum {

    MEMBER_MOBILE_EXIST("手机号已存在");

    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "desc='" + desc + '\'' +
                '}';
    }

    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }
}