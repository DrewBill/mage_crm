package com.mage.crm.dao;

import com.mage.crm.vo.Module;
import org.apache.ibatis.annotations.Select;

public interface ModuleDao {
    @Select("select id,module_name as moduleName,opt_value as optValue"
            + " from t_module where id=#{mid} and is_valid=1")
    Module queryModuleById(Integer moduleId);
}
