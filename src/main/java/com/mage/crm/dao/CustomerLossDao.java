package com.mage.crm.dao;

import com.mage.crm.query.CustomerLossQuery;
import com.mage.crm.vo.CustomerLoss;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface CustomerLossDao {
    @Update("update t_customer_loss set state=1 , confirm_loss_time = now(), loss_reason = #{reason} where id = #{id} ")
    Integer updateCustomerLossState(@Param("id") String id,@Param("reason") String reason);

    Integer insertBatch(List<CustomerLoss> customerLosses);

    List<CustomerLoss> queryCustomerLossesByParams(CustomerLossQuery customerLossQuery);

    Map<String, Object> queryCustomerLossesById(String lossId);
}
