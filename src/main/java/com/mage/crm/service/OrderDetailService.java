package com.mage.crm.service;

import com.mage.crm.dao.OrderDetailDao;
import com.mage.crm.vo.OrderDetail;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderDetailService {
    @Resource
    private OrderDetailDao orderDetailDao;

    public List<OrderDetail> queryOrderDetailsByOrderId(Integer orderId) {
        return orderDetailDao.queryOrderDetailsByOrderId(orderId);
    }
}
