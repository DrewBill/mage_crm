package com.mage.crm.dao;

import com.mage.crm.dto.UserDto;
import com.mage.crm.query.UserQuery;
import com.mage.crm.vo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDao {
    //通过用户名查询用户
    User queryUserByName(String userName);
    //通过用户id查询用户
    User queryUserById(String id);
    //修改密码
    int updatePwd(@Param("id") String id,@Param("userPwd") String userPwd);

    @Select("SELECT\n" +
            "\tu.true_name as 'trueName'\n" +
            "FROM\n" +
            "\tt_user u\n" +
            "LEFT JOIN t_user_role ur ON u.id = ur.user_id\n" +
            "LEFT JOIN t_role r ON r.id = ur.role_id\n" +
            "WHERE\n" +
            "\tr.role_name = '客户经理'\n" +
            "AND u.is_valid = 1\n" +
            "AND ur.is_valid = 1\n" +
            "AND r.is_valid = 1")
    List<User> queryAllCustomerManager();

    List<UserDto> queryUsersByParams(UserQuery userQuery);

    Integer insert(User user);

    Integer update(User user);

    @Delete("delete from t_user where id = #{id}")
    Integer delete(Integer id);
}
