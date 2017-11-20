package com.danke.entity;

import com.danke.enums.SexEnum;

import java.util.Date;

/**
 * userDemo Entity
 */
public class UserDemo {
    private int age;
    private Long id;
    private String userName;
    private SexEnum sex;
    private Date birthday;
    private Date createDate;

    public UserDemo() {
    }

    public UserDemo(String userName, int age, SexEnum sex, Date birthday, Date createDate) {
        this.userName = userName;
        this.age = age;
        this.sex = sex;
        this.birthday = birthday;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return this.getUserName();
    }
}
