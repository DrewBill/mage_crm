package com.mage.crm.dao;

import com.mage.crm.dto.ServeTypeDto;
import com.mage.crm.query.CustomerServeQuery;
import com.mage.crm.vo.CustomerServe;

import java.util.List;

public interface CustomerServerDao {
    Integer insert(CustomerServe customerServe);

    List<CustomerServe> queryCustomerServesByParams(CustomerServeQuery customerServeQuery);

    Integer update(CustomerServe customerServe);

    List<ServeTypeDto> queryCustomerServeType();
}
