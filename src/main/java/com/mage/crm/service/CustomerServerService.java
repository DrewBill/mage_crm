package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.base.ServeType;
import com.mage.crm.dao.CustomerServerDao;
import com.mage.crm.dto.ServeTypeDto;
import com.mage.crm.query.CustomerServeQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.CookieUtil;
import com.mage.crm.vo.CustomerServe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServerService {
    @Resource
    private CustomerServerDao customerServerDao;

    public void insert(CustomerServe customerServe) {
        checkCustomerServerParams(customerServe.getServeType(),customerServe.getCustomer(),customerServe.getServiceRequest());
        customerServe.setUpdateDate(new Date());
        customerServe.setCreateDate(new Date());
        customerServe.setIsValid(1);
        customerServe.setState(ServeType.CREATE.getType());
        AssertUtil.isTrue(customerServerDao.insert(customerServe)<1,"服务创建失败");
    }

    public void checkCustomerServerParams(String serveType,String customer,String serviceRequest){
        AssertUtil.isTrue(StringUtils.isBlank(serveType),"服务类型不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(customer),"客户名称！");
        AssertUtil.isTrue(StringUtils.isBlank(serviceRequest),"服务请求内容不能为空！");
    }

    public Map<String, Object> queryCustomerServesByParams(CustomerServeQuery customerServeQuery) {
        PageHelper.startPage(customerServeQuery.getPage(),customerServeQuery.getRows());
        List<CustomerServe> customerServeList = customerServerDao.queryCustomerServesByParams(customerServeQuery);
        PageInfo<CustomerServe> customerServePageInfo = new PageInfo<>(customerServeList);
        Map<String, Object> map = new HashMap<>();
        map.put("total",customerServePageInfo.getTotal());
        map.put("rows",customerServePageInfo.getList());
        return map;
    }

    public void update(CustomerServe customerServe, HttpServletRequest request) {
        String msg="";
        customerServe.setUpdateDate(new Date());
        if (customerServe.getState().equals(ServeType.ASSIGN.getType())){
            customerServe.setAssignTime(new Date());
            customerServe.setAssigner(CookieUtil.getCookieValue(request,"trueName"));
            msg="服务分配失败";
        }else if (customerServe.getState().equals(ServeType.PROCEED.getType())){
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProce()),"处理内容不能为空");
            customerServe.setServiceProceTime(new Date());
            msg="服务处理失败";
        }else if (customerServe.getState().equals(ServeType.FEEDBACK.getType())){
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getMyd()),"满意度不能为空");
            customerServe.setState(ServeType.ARCHIVE.getType());
        }
        AssertUtil.isTrue(customerServerDao.update(customerServe)<1,msg);
    }

    public Map<String, Object> queryCustomerServeType() {
        List<ServeTypeDto> serveTypeDtoList = customerServerDao.queryCustomerServeType();
        Map<String, Object> map = new HashMap<>();
        map.put("code",300);
        String[] types;
        ServeTypeDto[] datas;
        if (serveTypeDtoList!=null&&serveTypeDtoList.size()>0){
            types=new String[serveTypeDtoList.size()];
            datas=new ServeTypeDto[serveTypeDtoList.size()];
            for (int i=0;i<serveTypeDtoList.size();i++){
                types[i]=serveTypeDtoList.get(i).getName();
                datas[i]=serveTypeDtoList.get(i);
            }
            map.put("code",200);
            map.put("types",types);
            map.put("datas",datas);
        }
        return map;
    }
}
