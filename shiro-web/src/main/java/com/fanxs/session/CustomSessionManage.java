package com.fanxs.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @author fanxs
 * @date 2021/9/16
 */
public class CustomSessionManage extends DefaultWebSessionManager {
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;

        if (sessionKey instanceof WebSessionKey){
            request = ((WebSessionKey)sessionKey).getServletRequest();
        }
        if(request != null && sessionId != null){
            //从request中获取session，如果有，则直接返回。
            Session session = (Session)request.getAttribute(sessionId.toString());
            if(session != null ){
                return session;
            }
        }

        Session session = super.retrieveSession(sessionKey);
        if(request != null && sessionId != null){
            //将从redis中获取到的session存入request中，方便下次获取
            request.setAttribute(sessionId.toString(), session);
        }

        return session;
    }
}
