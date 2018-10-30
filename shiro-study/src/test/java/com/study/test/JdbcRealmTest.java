package com.study.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;

public class JdbcRealmTest {
	
	DruidDataSource datasource=new DruidDataSource();
	
	//使用构造代码块进行初始设置。代码块的执行顺序：静态代码块>构造代码块>构造方法
	{
		datasource.setUrl("jdbc:mysql://localhost:3306/test");
		datasource.setUsername("root");
		datasource.setPassword("");
		
	}

	
	@Test
	public void testAuthentication() {
		
		JdbcRealm realm = new JdbcRealm();
		realm.setDataSource(datasource);
		//默认为false，为false的时候不会去查询权限数据
		realm.setPermissionsLookupEnabled(true);
		
		
		//1、構建securityManager環境
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		securityManager.setRealm(realm);
		
		//2、主体提交认证请求
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		
		/*UsernamePasswordToken token = new UsernamePasswordToken("dujian", "123456");
		subject.login(token);
		
		System.out.println("isAuthenticated："+subject.isAuthenticated());
		//3、检测是否有某种角色
		subject.checkRole("admin");
		//这里查询会报错：Subject does not have permission [user:select]，是因为JdbcRealm需要设置PermissionsLookupEnabled为true，默认是false。
		//4、检测是否有某种权限
		subject.checkPermission("user:select");*/
		
		
		//抛弃默认的sql，使用自己写的sql语句进行查询
				String sql="Select password from test_user where username=?";
				realm.setAuthenticationQuery(sql);
				UsernamePasswordToken token = new UsernamePasswordToken("songqing", "654321");
				subject.login(token);
				
				System.out.println("isAuthenticated："+subject.isAuthenticated());
	}
}
