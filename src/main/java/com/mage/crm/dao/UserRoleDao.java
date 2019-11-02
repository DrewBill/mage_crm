package com.mage.crm.dao;

import com.mage.crm.vo.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleDao {
    Integer insertBatch(List<UserRole> userRoles);

    Integer queryUserRoleCountsByUserId(Integer id);

    Integer deleteUserRolesByUserId(Integer i);

    Integer queryUserRoleCountsByRoleId(Integer roleId);

    @Delete("delete from t_user_role where role_id=#{roleId}")
    Integer deleteUserRolesByRoleId(@Param("roleId") Integer roleId);
}
