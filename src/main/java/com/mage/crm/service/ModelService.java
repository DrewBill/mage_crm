package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.dao.ModelDao;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModelService {
    @Resource
    private ModelDao modelDao;
    @Resource
    private PermissionDao permissionDao;

    public List<ModuleDto> queryAllsModuleDtos(Integer rid){
        //查询到所有的资源
        List<ModuleDto> moduleDtoList = modelDao.queryAllsModuleDtos();
        //做一个勾选的问题
        //查询permssion根据rid得到modelId
        List<Integer> moduleIds = permissionDao.queryPermissionModuleIdsByRid(rid);
        if (moduleIds!=null&&moduleIds.size()>0){
            for (ModuleDto moduleDto:moduleDtoList){
                Integer mid = moduleDto.getId();
                if (moduleIds.contains(mid)){
                    moduleDto.setChecked(true);//节点选中
                }
            }
        }
        return moduleDtoList;
    }

    public Map<String, Object> queryModulesByParams(ModuleQuery moduleQuery) {
        PageHelper.startPage(moduleQuery.getPage(),moduleQuery.getRows());
        List<Module> moduleList = modelDao.queryModulesByParams(moduleQuery);
        PageInfo<Module> modulePageInfo = new PageInfo<>(moduleList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",modulePageInfo.getTotal());
        map.put("rows",modulePageInfo.getList());
        return map;
    }

    public List<Module> queryModulesByGrade(Integer grade) {
        return modelDao.queryModulesByGrade(grade);
    }

    public void insert(Module module) {
        checkModuleParams(module.getModuleName(),module.getGrade(),module.getOptValue());
        AssertUtil.isTrue(null != modelDao.queryModuleByOptValue(module.getOptValue()), "权限值不能重复!");
        AssertUtil.isTrue(null != modelDao.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName()),"该层级模块已存在");
        if (module.getGrade() != 0) {
            AssertUtil.isTrue(null == modelDao.queryModuleByPid(module.getParentId()), "父级菜单不存在!");
        }
        module.setIsValid(1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(modelDao.insert(module)<1,"模块记录添加失败");
    }

    private void checkModuleParams(String moduleName, Integer grade,
                                   String optValue) {
        AssertUtil.isTrue(StringUtils.isBlank(moduleName), "模块名非空!");
        AssertUtil.isTrue(null == grade, "层级值非法!");
        Boolean flag = (grade != 0 && grade != 1 && grade != 2);
        AssertUtil.isTrue(flag, "层级值非法!");
        AssertUtil.isTrue(StringUtils.isBlank(optValue), "权限值非空!");
    }

    private void checkModuleParams(String moduleName, Integer grade,
                                   String optValue, Integer id) {
        checkModuleParams(moduleName, grade, optValue);
        AssertUtil.isTrue(null == id || null == modelDao.queryModuleById(id), "待更新模块不存在!");
    }

    public void update(Module module) {
        /**
         * 1.参数校验
         *    模块名称非空
         *    层级 非空
         *    模块权限值 非空
         *    id 记录必须存在
         * 2.权限值不能重复
         * 3.每一层  模块名称不能重复
         * 4.非根级菜单 上级菜单必须存在
         * 5.设置额外字段
         *     updateDate
         * 6.执行更新
         */
        checkModuleParams(module.getModuleName(), module.getGrade(), module.getOptValue(), module.getId());
        Module temp = modelDao.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(null!=temp&&temp.getId().equals(module.getId()),"权限值不能重复");
        temp=modelDao.queryModuleByGradeAndModuleName(module.getGrade(),module.getModuleName());
        AssertUtil.isTrue(null != temp && !temp.getId().equals(module.getId()), "该层级下模块名不能重复!");
        if (module.getGrade()!=0){
            AssertUtil.isTrue(null==modelDao.queryModuleByPid(module.getParentId()),"父级菜单不存在");
        }
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(modelDao.update(module) < 1, CrmConstant.OPS_FAILED_MSG);
    }
}
