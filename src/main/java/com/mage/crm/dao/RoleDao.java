package com.mage.crm.dao;

import com.mage.crm.vo.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RoleDao {
    @Select("select id,role_name as'roleName' from t_role where is_valid =1")
    List<Role> queryAllRoles();

    List<Role> queryRolesByParams(@Param("roleName") String roleName);

    @Select("select id,role_name as roleName,role_remark as roleRemark "
            + "from t_role where role_name=#{roleName} and is_valid=1")
    Role queryRoleByRoleName(@Param("roleName") String roleName);

    @Insert("insert into t_role(role_name,role_remark,create_date,update_date,is_valid)"
            + " values(#{roleName},#{roleRemark},#{createDate},#{updateDate},#{isValid})")
    Integer insert(Role role);

    @Select("select id,role_name as roleName,role_remark as roleRemark "
            + "from t_role where id=#{id} and is_valid=1")
    Role queryRoleById(String id);

    @Update("update t_role set role_name=#{roleName},role_remark=#{roleRemark}"
            + " where id=#{id} and is_valid=1")
    Integer update(Role role);

    @Delete("delete from t_role  where id=#{id} ")
    Integer delete(Integer id);
}
