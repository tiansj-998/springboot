package com.broada.demo.springbootmybatis.service;

import com.alibaba.fastjson.JSONObject;
import com.broada.demo.springbootmybatis.entity.User;

import java.util.List;

/**
 * @Author: Tsj
 * @Date: 2020/5/6 14:03
 */
public interface UserService {

    String addUser(JSONObject jsonObject);

    User getUserById(int id);

    List<User> getAllUsers();

    String updateUserById(JSONObject jsonObject);

    String deleteById(int id);
}
