package com.danke.enums;

/**
 * 性别枚举类
 */
public enum  SexEnum {
    MALE(0,"男"),
    FEMALE(1,"女");

    private int sex;
    private String sexName;

    SexEnum(int sex, String sexName) {
        this.sex = sex;
        this.sexName = sexName;
    }

    public int getSex() {
        return sex;
    }

    public String getSexName() {
        return sexName;
    }

}
