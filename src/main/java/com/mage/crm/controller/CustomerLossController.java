package com.mage.crm.controller;

import com.github.pagehelper.PageHelper;
import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.CustomerLossQuery;
import com.mage.crm.service.CustomerLossService;
import com.mage.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@RequestMapping("customer_loss")
@Controller
public class CustomerLossController extends BaseController {
    @Resource
    private CustomerLossService customerLossService;
    @RequestMapping("index")
    public String index(){
        return "customer_loss";
    }

    @RequestMapping("queryCustomerLossesByParams")
    @ResponseBody
    public Map<String,Object> queryCustomerLossesByParams(CustomerLossQuery customerLossQuery){
        return customerLossService.queryCustomerLossesByParams(customerLossQuery);
    }

    @RequestMapping("toRepreivePage/{lossId}")
    public String toRepreivePage(@PathVariable("lossId") String lossId, Model model){
        model.addAttribute("customerLoss",customerLossService.queryCustomerLossesById(lossId));
        return "customer_repri";
    }

    @ResponseBody
    @RequestMapping("updateCustomerLossState")
    public MessageModel updateCustomerLossState(String lossId,String lossReason){
        customerLossService.updateCustomerLossState(lossId,lossReason);
        return createMessageModel("确认客户流失已经成功");
    }
}
