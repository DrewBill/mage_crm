package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerLossDao;
import com.mage.crm.dao.CustomerRepriDao;
import com.mage.crm.query.CustomerRepriQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.CustomerReprieve;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerRepriService {
    @Resource
    private CustomerRepriDao customerRepriDao;
    @Resource
    private CustomerLossDao customerLossDao;

    public Map<String, Object> customerReprieveByLossId(CustomerRepriQuery customerRepriQuery) {
        PageHelper.startPage(customerRepriQuery.getPage(),customerRepriQuery.getRows());
        List<CustomerReprieve> customerReprieveList = customerRepriDao.queryRepriByLossId(customerRepriQuery.lossId);
        PageInfo<CustomerReprieve> reprievePageInfo = new PageInfo<>(customerReprieveList);
        Map<String, Object> map = new HashMap<>();
        map.put("total",reprievePageInfo.getTotal());
        map.put("rows",reprievePageInfo.getList());
        return map;
    }

    public void insertReprive(CustomerReprieve customerReprieve) {
        checkParams(customerReprieve.getMeasure(),customerReprieve.getLossId());
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());
        customerReprieve.setIsValid(1);
        AssertUtil.isTrue(customerRepriDao.insert(customerReprieve)<1,"暂缓措施添加失败");
    }

    public void checkParams(String measure,Integer lossId){
        AssertUtil.isTrue(StringUtils.isBlank(measure),"暂缓措施不能为空");
        Map<String, Object> map = customerLossDao.queryCustomerLossesById(lossId + "");
        AssertUtil.isTrue(lossId==null||map==null||map.isEmpty(),"流失记录不存在");
    }

    public void delete(Integer id) {
        AssertUtil.isTrue(customerRepriDao.delete(id)<1,"暂缓措施删除失败");
    }

    public void update(CustomerReprieve customerReprieve) {
        checkParams(customerReprieve.getMeasure(),customerReprieve.getLossId());
        customerReprieve.setUpdateDate(new Date());
        AssertUtil.isTrue(customerRepriDao.update(customerReprieve)<1,"暂缓措施修改失败");
    }
}
