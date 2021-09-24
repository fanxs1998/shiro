package com.fanxs.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 过滤器
 * 授权相关filter继承AuthorizationFilter
 * @date 2021/9/16
 */
public class RolesOrFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        String[] roles = (String[]) mappedValue;
        if (roles == null || roles.length == 0){
            return true;
        }

        //有其中一个权限就算成功
        for (String role : roles){
            if(subject.hasRole(role)){
                return true;
            }
        }
        return false;
    }
}
