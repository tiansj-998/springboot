package com.broada.demo.springbootmybatis.mapper;

import com.broada.demo.springbootmybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Tsj
 * @Date: 2020/5/6 14:49
 */
@Mapper
@Repository
public interface UserMapper {

    void insertUser(User user);

    User queryUserById(@Param("id") int id);

    List<User> queryAllUser();

    void updateUserById(int id, String name, String password);

    void deleteById(@Param("id") int id);
}
