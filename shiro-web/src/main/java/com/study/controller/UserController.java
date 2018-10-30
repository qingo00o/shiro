package com.study.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.vo.User;

@Controller
public class UserController {

	/**
	 * 登录验证
	 * @Title subLogin
	 * @param user
	 * @return String
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	@RequestMapping(value="/subLogin",method=RequestMethod.POST, produces="application/json;charset=utf-8")
	@ResponseBody
	public String subLogin(User user) {
		
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
		try {
			
			token.setRememberMe(user.isRememberMe());
			subject.login(token);
		} catch (Exception e) {
			return e.getMessage();
		}
		//判断主体有没有某种权限
		if (subject.hasRole("admin")) {
			if (subject.isPermitted("user:select")) {
				return "主体拥有admin的对于user查询的权限";
			}
			return "主体拥有admin的权限";
			
		}
		
		return "登陆成功";
	}
	
	/**
	 * 测试shiro过滤器进行角色授权
	 * @Title testShiroFilterRole
	 * @return String
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	@RequestMapping(value="/testShiroFilterRole",method=RequestMethod.GET)
	@ResponseBody
	public String testShiroFilterRole() {
		return "testShiroFilterRole success";
	}
	
	/**
	 * 测试shiro过滤器进行多个 角色权限验证
	 * @Title testShiroFilterRole1
	 * @return String
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	@RequestMapping(value="/testShiroFilterRole1",method=RequestMethod.GET)
	@ResponseBody
	public String testShiroFilterRole1() {
		return "testShiroFilterRole1 success";
	}
	
	/**
	 * 测试shiro过滤器进行资源授权
	 * @Title testShiroFilterPermission
	 * @return String
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	@RequestMapping(value="/testShiroFilterPermission",method=RequestMethod.GET)
	@ResponseBody
	public String testShiroFilterPermission() {
		return "testShiroFilterPermission success";
	}
	
	/**
	 * 测试注解式角色授权
	 * @Title testRole
	 * @return String
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	@RequestMapping(value="/testRole",method=RequestMethod.GET)
	@ResponseBody
	@RequiresRoles("admin")
	public String testRole() {
		return "testRole success";
	}
	/**
	 * 测试注解式角色授权
	 * @Title testRole1
	 * @return String
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	@RequestMapping(value="/testRole1",method=RequestMethod.GET)
	@ResponseBody
	@RequiresRoles("admin1")
	public String testRole1() {
		return "testRole1 success";
	}
	
	/**
	 * 测试注解式资源授权
	 * @Title testPermission
	 * @return String
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	@RequestMapping(value="/testPermission",method=RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions("user:select")
	public String testPermission() {
		return "testPermission success";
	}
	/**
	 * 测试注解式资源授权
	 * @Title testPermission1
	 * @return String
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	@RequestMapping(value="/testPermission1",method=RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions({"user:select","user:add"})
	public String testPermission1() {
		return "testPermission1 success";
	}
	
	@RequestMapping("/unauthorized")
	public String unauthorized() {
		return "/403.html";
	}
	
}
