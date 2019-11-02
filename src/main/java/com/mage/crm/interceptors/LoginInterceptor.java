package com.mage.crm.interceptors;

import com.mage.crm.base.CrmConstant;
import com.mage.crm.service.UserService;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.Base64Util;
import com.mage.crm.util.CookieUtil;
import com.mage.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //得到Cookie中的id
        String id= CookieUtil.getCookieValue(request,"id");
        System.out.println(StringUtils.isBlank(id));
        System.out.println(id);
        AssertUtil.isTrue(StringUtils.isBlank(id), CrmConstant.LOGIN_NO_CODE,CrmConstant.LOGIN_NO_MSG);
        //通过id查询是否有user对象
        User user = userService.queryUserById(Base64Util.deCode(id));//对cookie的id进行解密
        //判断user是否存在 为null
        System.out.println(user);
        AssertUtil.isTrue(null==user,CrmConstant.LOGIN_NO_CODE,CrmConstant.LOGIN_NO_MSG);
        //判断user是否有效
        AssertUtil.isTrue("0".equals(user.getIsValid()),CrmConstant.LOGIN_NO_CODE,CrmConstant.LOGIN_NO_MSG);
        return true;
    }

}
