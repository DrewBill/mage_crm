package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerLossDao;
import com.mage.crm.query.CustomerLossQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.CustomerLoss;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerLossService {
    @Resource
    private CustomerLossDao customerLossDao;

    public Map<String, Object> queryCustomerLossesByParams(CustomerLossQuery customerLossQuery) {
        PageHelper.startPage(customerLossQuery.getPage(),customerLossQuery.getRows());
        List<CustomerLoss> customerLossList = customerLossDao.queryCustomerLossesByParams(customerLossQuery);
        PageInfo<CustomerLoss> customerLossPageInfo = new PageInfo<>(customerLossList);
        Map<String, Object> map = new HashMap<>();
        map.put("total",customerLossPageInfo.getTotal());
        map.put("rows",customerLossPageInfo.getList());
        return map;
    }

    public Map<String,Object> queryCustomerLossesById(String lossId) {
        return customerLossDao.queryCustomerLossesById(lossId);
    }

    public void updateCustomerLossState(String lossId, String lossReason) {
        AssertUtil.isTrue(StringUtils.isBlank(lossReason),"流失原因不能为空");
        Map<String, Object> map = customerLossDao.queryCustomerLossesById(lossId);
        AssertUtil.isTrue(map==null||map.isEmpty(),"流失记录不存在");
        AssertUtil.isTrue(customerLossDao.updateCustomerLossState(lossId,lossReason)<1,"操作失败");
    }
}
