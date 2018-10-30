package com.study.session;

import java.io.Serializable;

import javax.servlet.ServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
/**
 * @Description 实现自定义的SessionManager，解决每次请求都要多次读取redis的问题
 * @author DuJian
 * @Date 2018年9月28日上午9:44:11
 */
public class CustomSessionManager extends DefaultWebSessionManager{

	@Override
	protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
		// sessionKey中存储了request和response对象，所以我们可以在第一次去redis读取的时候就将session放入request中
		Serializable sessionId = getSessionId(sessionKey);
		ServletRequest request=null;
		
		if (sessionKey instanceof WebSessionKey) {
			request=((WebSessionKey)sessionKey).getServletRequest();
		}
		
		if (request !=null && sessionId !=null) {
			Session session = (Session) request.getAttribute(sessionId.toString());
			if (session!=null) {
				return session;
			}
		
		}
		Session session =  super.retrieveSession(sessionKey);
		if (request !=null && sessionId !=null) {
			request.setAttribute(sessionId.toString(), session);
		}
		
		return session;
	}
}
