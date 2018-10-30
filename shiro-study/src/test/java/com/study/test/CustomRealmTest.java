package com.study.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.study.shiro.realm.CustomRealm;

/**
 * @Description 测试CustomRealm
 * @author DuJian
 * @Date 2018年9月25日下午4:25:23
 */
public class CustomRealmTest {

	
	@Test
	public void testAuthentication() {
		
		CustomRealm realm=new CustomRealm();
		
		//1、構建securityManager環境
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		
		securityManager.setRealm(realm);
		//这里设置加密密码以便和数据库中比对
		HashedCredentialsMatcher  matcher=new HashedCredentialsMatcher();
		matcher.setHashAlgorithmName("md5");
		matcher.setHashIterations(1);
		realm.setCredentialsMatcher(matcher);
		//2、主体提交认证请求
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("dujian", "123456");
		subject.login(token);
		
		System.out.println("isAuthenticated："+subject.isAuthenticated());
		
		subject.checkRole("admin");
		subject.checkPermission("user:add");
		
	}
}
