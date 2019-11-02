package com.mage.crm.dao;

import com.mage.crm.vo.CustomerReprieve;

import java.util.List;

public interface CustomerRepriDao {

    List<CustomerReprieve> queryRepriByLossId(String lossId);

    Integer insert(CustomerReprieve customerReprieve);

    Integer delete(Integer id);

    Integer update(CustomerReprieve customerReprieve);
}
