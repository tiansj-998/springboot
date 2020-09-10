package com.broada.demo.springbootmybatis.entity;

/**
 * @Author: Tsj
 * @Date: 2020/5/6 14:47
 */
public class Response {
    String msg;
    int code;

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "{msg='" + msg + '\'' +
                ", code=" + code +
                '}';
    }
}
