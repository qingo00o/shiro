package com.study.shiro.realm;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
/**
 * @Description 实现自定义realm
 * @author DuJian
 * @Date 2018年9月25日下午4:11:30
 */
public class CustomRealm extends AuthorizingRealm{
	
	Map<String, String> usermap=new HashedMap(16);
	{
		//给到加密后的密码
		usermap.put("dujian", "e10adc3949ba59abbe56e057f20f883e");
		//设置RealmName
		super.setName("customRealm");
	}

	@Override
	/**
	 * 授权
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		//1、通过主体传来的认证信息中获取用户名
				String  username = (String) principals.getPrimaryPrincipal();
				
				//2、从数据库或者缓存中获取角色和权限
				Set<String> roles= getRolesByUsernames(username);
				
				Set<String> permissions= getPermissionsByUsernames(username);
		
				//3、返回授权信息
				SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(roles);
				authorizationInfo.setStringPermissions(permissions);
				
				return authorizationInfo;
	}

	/**
	 * 模拟根据用户名去数据库或者缓存中查询用户权限
	 * @Title getPermissionsByUsernames
	 * @param username
	 * @return Set<String>
	 * @author Dujian
	 * @Date 2018年9月25日
	 */
	private Set<String> getPermissionsByUsernames(String username) {
		Set<String> permissions=new HashSet<>();
		
		permissions.add("user:delete");
		permissions.add("user:add");
		return permissions;
	}

	/**
	 * 模拟根据用户名去数据库或者缓存中查询用户角色
	 * @Title getRolesByUsernames
	 * @param username
	 * @return Set<String>
	 * @author Dujian
	 * @Date 2018年9月25日
	 */
	private Set<String> getRolesByUsernames(String username) {
		Set<String> sets=new HashSet<>();
		sets.add("admin");
		sets.add("user");
		return sets;
	}

	@Override
	/**
	 * 认证
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1、通过主体传来的认证信息中获取用户名
		String  username = (String) token.getPrincipal();
		//2、通过用户名到数据库获取凭证
		String password = getPasswordByUsername(username);
		if (password==null) {
			return null;
		}
		//3、返回认证信息
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("dujian", password, "customRealm");
		return authenticationInfo;
	}

	/**
	 * 模拟去数据库查询密码
	 * @Title getPasswordByUsername
	 * @param username
	 * @return String
	 * @author Dujian
	 * @Date 2018年9月25日
	 */
	private String getPasswordByUsername(String username) {
		return usermap.get(username);
		
	}
	
	
	public static void main(String[] args) {
		Md5Hash md5Hash = new Md5Hash("123456");
		System.out.println(md5Hash);//输出：e10adc3949ba59abbe56e057f20f883e
	}

}
