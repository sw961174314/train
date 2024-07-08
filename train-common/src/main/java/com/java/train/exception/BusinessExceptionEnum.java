package com.java.train.exception;

public enum BusinessExceptionEnum {

    MEMBER_MOBILE_EXIST("手机号已注册"),
    MEMBER_MOBILE_NOT_EXIST("请先获取短信验证码"),
    MEMBER_MOBILE_CODE_EXIST("短信验证码错误"),

    BUSINESS_STATION_NAME_UNIQUE_ERROR("车站已存在");

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