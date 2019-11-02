package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.ModuleDao;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.RoleDao;
import com.mage.crm.dao.UserRoleDao;
import com.mage.crm.query.RoleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Module;
import com.mage.crm.vo.Permission;
import com.mage.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService {
    @Resource
    private RoleDao roleDao;

    @Resource
    private UserRoleDao userRoleDao;

    @Resource
    private PermissionDao permissionDao;

    @Resource
    private ModuleDao moduleDao;

    public List<Role> queryAllRoles() {
        return roleDao.queryAllRoles();
    }

    public Map<String, Object> queryRolesByParams(RoleQuery roleQuery) {
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getRows());
        List<Role> roleList = roleDao.queryRolesByParams(roleQuery.getRoleName());
        PageInfo<Role> rolePageInfo = new PageInfo<>(roleList);
        Map<String, Object> map = new HashMap<>();
        map.put("total",rolePageInfo.getTotal());
        map.put("rows",rolePageInfo.getList());
        return map;
    }

    public void insert(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        AssertUtil.isTrue(roleDao.queryRoleByRoleName(role.getRoleName())!=null,"角色已存在");
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        AssertUtil.isTrue(roleDao.insert(role)<1,"角色添加失败");
    }

    public void update(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        AssertUtil.isTrue(role.getId()==null||null==roleDao.queryRoleById(role.getId()),"待更新的记录不存在");
        Role tmp = roleDao.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null!=tmp&&tmp.getId().equals(role.getId()),"角色名已存在");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleDao.update(role)<1,"角色修改失败");
    }

    public void delete(Integer id) {
        AssertUtil.isTrue(null==id||null==roleDao.queryRoleById(id+""), "待删除的角色记录不存在!");
        //级联删除用户角色记录
        Integer count = userRoleDao.queryUserRoleCountsByRoleId(id);

        if (count>0){
            AssertUtil.isTrue(userRoleDao.deleteUserRolesByRoleId(id)<count,"用户角色级联删除失败");
        }
        AssertUtil.isTrue(roleDao.delete(id)<1,"角色删除失败");
    }

    /**
     * 1.参数合法性校验
     *    rid 角色记录必须存在
     *    moduleIds  可空
     * 2.删除原始权限
     *     查询原始权限
     *         原始权限存在  执行删除操作
     * 3. 执行批量添加
     *     根据moduleId  查询每个模块  权限值
     *     List<Permission>
     */
    public void addPermission(Integer rid, Integer[] moduleIds) {
        AssertUtil.isTrue(null==rid||null==roleDao.queryRoleById(rid+""),"待授权的角色不存在");
        Integer count = permissionDao.queryPermissionCountByRid(rid);
        if (count>0){
            AssertUtil.isTrue(permissionDao.deletePermissionByRid(rid)<count,"删除原始权限失败");
        }
        List<Permission> permissions=null;
        if (null!=moduleIds&&moduleIds.length>0){
            //执行批量添加
            permissions=new ArrayList<>();
            Module module=null;
            for (Integer moduleId:moduleIds){
                module = moduleDao.queryModuleById(moduleId);
                //组装permission对象
                Permission permission = new Permission();
                if (null!=module){
                    permission.setAclValue(module.getOptValue());
                }
                permission.setRoleId(rid);
                permission.setModuleId(moduleId);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                //放到list集合中
                permissions.add(permission);
            }
            AssertUtil.isTrue(permissionDao.insertBatch(permissions)<moduleIds.length,"角色权限添加失败");
        }
    }
}
