package com.broada.demo.springbootmybatis.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Tsj
 * @date 2020/5/6 13:45
 */
public class User implements Serializable {

    int id;
    String name;
    String password;
    Timestamp createTime;

    public User() {}

    public User(JSONObject jsonObject){
        if(jsonObject.containsKey("id")){
            this.id = jsonObject.getIntValue("id");
        }
        this.name = jsonObject.getString("name");
        this.password = jsonObject.getString("password");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
