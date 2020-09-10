package com.broada.demo.springbootmybatis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.broada.demo.springbootmybatis.entity.Response;
import com.broada.demo.springbootmybatis.entity.User;
import com.broada.demo.springbootmybatis.mapper.UserMapper;
import com.broada.demo.springbootmybatis.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author: Tsj
 * @Date: 2020/5/6 14:04
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String NAME = "name";
    private static final String PASSWORD = "password";

    @Autowired
    private UserMapper userMapper;

    @Override
    public String addUser(JSONObject jsonObject) {
        User user = new User();
        user.setName(jsonObject.getString(NAME));
        user.setPassword(jsonObject.getString(PASSWORD));
        logger.info(user.toString());
        try {
            userMapper.insertUser(user);
            logger.info("添加user成功,名字是{}",user.getName());
            Response res = new Response(200,"添加成功");
            return res.toString();
        }catch (Exception e){
            logger.error("添加user时出错",e);
            return "error occured during addUser!";
        }
    }

    @Override
    public User getUserById(int id) {
        try {
            User user = userMapper.queryUserById(id);
            logger.info("获取ID为[{}]的user成功",id);
            return user;
        }catch (Exception e){
            logger.error("获取ID为[{}]的user失败",id,e);
            return null;
        }
    }

    @Override
    public List<User> getAllUsers(){
        try {
            List<User> users = userMapper.queryAllUser();
            if(users.size() == 0){
                logger.error("获取users为空！");
                return null;
            }
            logger.info("获取users成功,共有{}个User",users.size());
            return users;
        }catch (Exception e){
            logger.error("获取user时出错[{}]",e.getMessage(),e);
            return null;
        }
    }

    @Override
    public String updateUserById(JSONObject jsonObject) {
        try {
            User user = new User(jsonObject);
            userMapper.updateUserById(user.getId(),user.getName(),user.getPassword());
            logger.info("修改ID为{}的User成功,修改后name为{}",user.getId(),user.getName());
            return "update success";
        }catch (Exception e){
            logger.error("修改user失败,reason[{}]",e.getMessage(),e);
            return "update failed";
        }
    }

    @Override
    public String deleteById(int id) {
        try {
            userMapper.deleteById(id);
            logger.info("删除ID为{}的User成功",id);
            return "delete success";
        }catch (Exception e){
            logger.error("删除user失败,reason[{}]",e.getMessage(),e);
            return "delete failed";
        }
    }
}
