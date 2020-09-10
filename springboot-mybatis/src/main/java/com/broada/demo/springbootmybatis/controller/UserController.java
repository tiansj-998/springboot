package com.broada.demo.springbootmybatis.controller;

import com.alibaba.fastjson.JSONObject;
import com.broada.demo.springbootmybatis.entity.User;
import com.broada.demo.springbootmybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Tsj
 * @Date: 2020/5/6 14:06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String addUser(@RequestBody JSONObject jsonObject){
        return userService.addUser(jsonObject);
    }

    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    public User getUserById(int id){
        return userService.getUserById(id);
    }

    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/deleteById",method = RequestMethod.GET)
    public String deleteUserById(int id){
        return userService.deleteById(id);
    }

    @RequestMapping(value = "/updateById",method = RequestMethod.POST)
    public String updateUserById(@RequestBody JSONObject jsonObject){
        return userService.updateUserById(jsonObject);
    }
}
