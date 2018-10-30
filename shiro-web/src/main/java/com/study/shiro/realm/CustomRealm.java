package com.study.shiro.realm;

import java.util.HashSet;
import java.util.List;
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
import org.apache.shiro.util.ByteSource;
/**
 * @Description 实现自定义realm
 * @author DuJian
 * @Date 2018年9月25日下午4:11:30
 */
import org.springframework.beans.factory.annotation.Autowired;

import com.study.dao.impl.UserDao;
import com.study.vo.User;
public class CustomRealm extends AuthorizingRealm{
	
	@Autowired
	private UserDao userDao;

	@Override
	/**
	 * 授权
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		//1、通过主体传来的认证信息中获取用户名
				String  username = (String) principals.getPrimaryPrincipal();
				System.out.println("从数据库获取权限数据..................");
				//2、从数据库或者缓存中获取角色和权限
				Set<String> roles= getRolesByUsernames(username);
				
				Set<String> permissions= getPermissionsByUsernames(username);
		
				//3、返回授权信息
				SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(roles);
				authorizationInfo.setStringPermissions(permissions);
				
				return authorizationInfo;
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
		//3、得到认证信息
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, "customRealm");
		
		//4、在返回authenticationInfo之前加盐,这里用的是用户名，一般直接用随机数
		authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username));
		return authenticationInfo;
	}

	/**
	 * 去数据库查询密码
	 * @Title getPasswordByUsername
	 * @param username
	 * @return String
	 * @author Dujian
	 * @Date 2018年9月25日
	 */
	private String getPasswordByUsername(String username) {
		
		User user = userDao.getUserByUsername(username);
		if (user!=null) {
					return user.getPassword();
		}
		return null;
	}
	
	/**
	 * 根据用户名去数据库或者缓存中查询用户角色
	 * @Title getRolesByUsernames
	 * @param username
	 * @return Set<String>
	 * @author Dujian
	 * @Date 2018年9月25日
	 */
	private Set<String> getRolesByUsernames(String username) {
		List<String> list = userDao.getRolesByUsername(username);
		Set<String> sets=new HashSet<>(list);
		
		return sets;
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
		
		List<String> permissionsList = userDao.getPermissionsByUsernames(username);
		Set<String> permissions=new HashSet<>(permissionsList);
		
		return permissions;
	}

	
	public static void main(String[] args) {
		Md5Hash md5Hash = new Md5Hash("123456","dujian");
		System.out.println(md5Hash);//输出：75a00ab0ab7a3fff91862356e5c674c5
	}

}
