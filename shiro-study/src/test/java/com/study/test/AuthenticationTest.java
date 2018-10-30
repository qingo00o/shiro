package com.study.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationTest {
	
	
	SimpleAccountRealm realm=new SimpleAccountRealm();
	
	@Before
	public void addUser() {
		realm.addAccount("dujian", "123456","admin");
	}

	@Test
	public void testAuthentication() {
		//1、構建securityManager環境
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		securityManager.setRealm(realm);
		//2、主体提交认证请求
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("dujian", "123456");
		subject.login(token);
		
		System.out.println("isAuthenticated："+subject.isAuthenticated());
		subject.checkRole("admin");
		
	}
}
