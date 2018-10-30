package com.study.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.study.vo.User;

@Component
public class UserDaoImpl implements UserDao {
	
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	/**
	 * 根据用户名获取用户信息
	 */
	public User getUserByUsername(String username) {
		String sql="select username,password from users where username=?";
		
		List<User> list = jdbcTemplate.query(sql, new String[] {username},new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet resultSet, int i) throws SQLException {
				User user=new User();
				user.setUsername(resultSet.getString("username"));
				user.setPassword(resultSet.getString("password"));
				return user;
			}
			
		});
		
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	@Override
	/**
	 * 根据用户名获取角色信息
	 */
	public List<String> getRolesByUsername(String username) {
		String sql="Select role_name from user_roles where username=?";
		List<String> list = jdbcTemplate.query(sql, new String[] {username},new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("role_name");
			}
			
		});
		
		return list;
	}

	@Override
	/**
	 * 根据用户名获取权限信息
	 */
	public List<String> getPermissionsByUsernames(String username) {
		String sql="SELECT rp.permission FROM `roles_permissions` rp WHERE rp.role_name in "
				+ "(SELECT ur.role_name FROM user_roles ur WHERE ur.username=?)";
		
		/*List<String> list = jdbcTemplate.query(sql, new String[] {username},new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("permission");
			}
			
		});*/
		//可以使用lambda表达式
		List<String> list = jdbcTemplate.query(sql, new String[] {username},
				((ResultSet rs, int rowNum)->rs.getString("permission")));
		return list;
	}

}
