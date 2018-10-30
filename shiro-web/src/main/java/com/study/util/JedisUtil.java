package com.study.util;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Description 访问redis工具类
 * @author DuJian
 * @Date 2018年9月27日下午5:07:51
 */
@Component
public class JedisUtil {

	@Resource
	private JedisPool jedisPool;
	
	private Jedis getResource() {
		return jedisPool.getResource();
	}

	/**
	 * 创建一个键值对
	 * @Title set
	 * @param key
	 * @param value
	 * @return byte[]
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	public byte[] set(byte[] key, byte[] value) {
		Jedis jedis = getResource();
		try {
			jedis.set(key, value);
			return value;
		} finally {
			jedis.close();
		}
	}

	/**
	 * 设置超时时间
	 * @Title expire
	 * @param key
	 * @param i void
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	public void expire(byte[] key, int i) {
		Jedis jedis = getResource();
		try {
			jedis.expire(key, i);
		} finally {
			jedis.close();
		}
		
	}

	/**
	 * 通过key获取value
	 * @Title get
	 * @param key
	 * @return byte[]
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	public byte[] get(byte[] key) {
		Jedis jedis = getResource();
		try {
			return jedis.get(key);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 删除会话
	 * @Title del
	 * @param key void
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	public void del(byte[] key) {
		// TODO Auto-generated method stub
		Jedis jedis = getResource();
		try {
			 jedis.del(key);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 获取指定前缀的所有key
	 * @Title keys
	 * @param shiro_session_prefix
	 * @return Set<byte[]>
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	public Set<byte[]> keys(String shiro_session_prefix) {
		// TODO Auto-generated method stub
		Jedis jedis = getResource();
		try {
			return jedis.keys((shiro_session_prefix+"*").getBytes());
		} finally {
			jedis.close();
		}
		
	}
	
}
