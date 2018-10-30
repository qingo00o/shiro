package com.study.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * @Description 创建自定义的权限过滤器
 * 这里的过滤器是和授权相关的，继承AuthorizationFilter，如果是和认证相关的，就继承AuthenticationFilter
 * @author DuJian
 * @Date 2018年9月27日下午3:51:43
 */
public class RolesOrFilter extends AuthorizationFilter{

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		//AuthorizationFilter的父类AccessControlFilter定义了直接获得主体的方法，如下
		Subject subject = getSubject(request, response);
		//mappedValue即是角色数组
		String[] 	roles=(String[]) mappedValue;
		
		if (roles==null || roles.length==0) {//说明不需要任何权限就可以访问
			return true;
		}
		//遍历访问需要的权限数组，如果subject中有其中之一，即返回true
		for (String role : roles) {
			if (subject.hasRole(role)) {
				return true;
			}
		}
		return false;
	}

	
	
}
