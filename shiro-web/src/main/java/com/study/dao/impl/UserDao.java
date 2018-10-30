package com.study.dao.impl;

import java.util.List;

import com.study.vo.User;

public interface UserDao {

		public User getUserByUsername(String username);
		
		public List<String> getRolesByUsername(String username);

		public List<String> getPermissionsByUsernames(String username);
}
