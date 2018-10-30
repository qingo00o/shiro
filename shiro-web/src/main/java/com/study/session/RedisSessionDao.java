package com.study.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.SerializationUtils;

import com.study.util.JedisUtil;

/**
 * @Description shiro的session管理
 * @author DuJian
 * @Date 2018年9月27日下午5:05:58
 */
public class RedisSessionDao  	extends AbstractSessionDAO{
	@Resource
	private JedisUtil jedisUtil;
	
	private final String shiro_session_prefix = "study-session:";
	
	/**
	 * 给sessionId的key加上前缀
	 * @Title getKey
	 * @param key
	 * @return byte[]
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	private byte[] getKey( String key ) {
		return (shiro_session_prefix + key).getBytes();
	}
	/**
	 * 保存session到redis缓存的方法
	 * @Title saveSession
	 * @param session void
	 * @author Dujian
	 * @Date 2018年9月27日
	 */
	private void saveSession(Session session) {
		if (session!=null && session.getId()!=null) {
			//key为sessionID
			byte[] key = getKey(session.getId().toString());
			//value为序列化的session
			byte[] value = SerializationUtils.serialize(session);
			
			jedisUtil.set(key,value);
			jedisUtil.expire(key,600);
		}
				
	}
	/**
	 * 创建session
	 */
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		//将sessionID和的session进行绑定
		assignSessionId(session, sessionId);
		saveSession(session);
		return sessionId;
	}

	/**
	 * 通过sessionID获得session
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		System.out.println("read session");
		if (sessionId==null) {
			return null;
		}
		byte[] key = getKey(sessionId.toString());
		byte[] value=jedisUtil.get(key);
		//将value反序列化为session
		return (Session)SerializationUtils.deserialize(value);
	}

	/**
	 * 更新会话
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		saveSession(session);
	}

	/**
	 * 删除会话
	 */
	@Override
	public void delete(Session session) {
		if (session==null && session.getId()==null) {
			return ;
		}
		byte[] key = getKey(session.getId().toString());
		jedisUtil.del(key);
		
	}

	/**
	 * 获取所有的存活的session
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		// TODO Auto-generated method stub
		
		Set<byte[]> keys= jedisUtil.keys(shiro_session_prefix);
		
		Set<Session> sessions=new HashSet<>();
		if (CollectionUtils.isEmpty(keys)) {
			return sessions;
		}
		for (byte[] key : keys) {
			Session session=(Session) SerializationUtils.deserialize(jedisUtil.get(key));
			sessions.add(session);
		}
		return sessions;
	}

	

}
