package com.broada.demo.springbootjdbc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.broada.demo.springbootjdbc.entity.User;
import com.broada.demo.springbootjdbc.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author tsj
 * @Date 2020/9/10 18:10
 */
@RestController
@RequestMapping("/jdbc")
public class JdbcController {

    private static final String DBNAME = "localtest";
    private static final Logger logger = LoggerFactory.getLogger(JdbcController.class);
    private static final DateFormat DF1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
    private static final DateFormat DF2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 查询所有用户
     * @return
     */
    @GetMapping("/queryAllUser")
    public ResponseEntity queryAllUser(){
        DbUtil.setDataSourceHolder(DBNAME);
        String sql = "select * from t_user";
        List<Map<String, Object>> usersList = null;
        try {
            usersList = DbUtil.queryForList(sql);
        }catch (Exception e){
            logger.error("查询用户时出错，error message:[{}]",e.getMessage(),e);
        }

        /*List<User> users = new ArrayList<>();
        for(Map map:usersList){
            User user = JSON.parseObject(JSON.toJSONString(map),User.class);
            users.add(user);
        }*/

        ResponseEntity re = new ResponseEntity(usersList,HttpStatus.OK);
        return re;
    }

    /**
     * 查询单个用户
     * @return
     */
    @GetMapping("/queryUser")
    public ResponseEntity queryUser(){
        DbUtil.setDataSourceHolder(DBNAME);
        String sql = "select * from t_user limit 1";
        Object o = null;
        try{
            o = DbUtil.queryForMap(sql);
        }catch (Exception e){
            logger.error("sql[{}]执行错误，message[{}]",sql,e.getMessage(),e);
        }
        return new ResponseEntity(o,HttpStatus.OK);
    }

    /**
     * 查询某个User的某个字段
     * @return
     */
    @GetMapping("/queryUserInfo")
    public ResponseEntity queryUserInfo(){
        DbUtil.setDataSourceHolder(DBNAME);
        String sql = "select * from t_user limit 1,1";
        Object o = null;
        try{
            o = DbUtil.queryForObject(sql, "name");
        }catch (Exception e){
            logger.error("sql[{}]执行错误，message[{}]",sql,e.getMessage(),e);
        }
        return new ResponseEntity(o,HttpStatus.OK);
    }

    /**
     * 新增User
     * @param payload
     * @return
     */
    @PostMapping("/insertUser")
    public String insertUser(@RequestBody JSONObject payload){
        DbUtil.setDataSourceHolder(DBNAME);
        String username = payload.getString("name");
        String pwd = payload.getString("pwd");
        String sql = String.format("insert into t_user (id,name,password,create_time) values (null,'%s','%s',null)",username,pwd);

        long l;
        try{
            l = DbUtil.executeInsert(sql);
        }catch (Exception e){
            logger.error("sql[{}]执行错误，message[{}]",sql,e.getMessage(),e);
            return "error ocurred";
        }
        return "insert user success\n response id:" + l;
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam int id,@RequestBody JSONObject payload){
        DbUtil.setDataSourceHolder(DBNAME);
        String name = payload.getString("name");
        String pwd = payload.getString("pwd");
        StringBuilder sbSQL = new StringBuilder("update t_user set ");
        if(name != null && !"".equals(name)){
            sbSQL.append("name = '"+name).append("'");
        }
        if(pwd != null && !"".equals(pwd)){
            if (sbSQL.toString().contains("name")) {
                sbSQL.append(",");
            } else {
                sbSQL.append(" ");
            }
            sbSQL.append("password = '" + pwd).append("'");
        }else{
            logger.info("修改后的pwd不能为空！");
            return "pwd can not be empty";
        }
        sbSQL.append(" where id = " + id);
        try{
            DbUtil.executeUpdate(sbSQL.toString());
        }catch (Exception e){
            logger.error("sql[{}]执行错误，message[{}]",sbSQL,e.getMessage(),e);
            return "error ocurred during executing sql, update failed!";
        }
        return "update user success";
    }

    @GetMapping("/deleteUserById")
    public String deleteUser(int id){
        DbUtil.setDataSourceHolder(DBNAME);
        String sql = "delete from t_user where id = "+ id;
        boolean result;
        try {
            result = DbUtil.executeDelete(sql);
        }catch (Exception e){
            logger.error("sql[{}]执行错误，message[{}]",sql,e.getMessage(),e);
            return "error ocurred during executing sql, delete failed!";
        }
        return result ? "delete success" : "delete failed";
    }
}
