package com.fanxs;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * @author fanxs
 * @date 2021/9/13
 */
public class AuthenticationTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("fxs","123456", "admin","user");
    }

    /**
     * 认证
     */
    @Test
    public void testAuthentication() {
        //1. 构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);
        /*也可从自定义配置文件shiro.ini获取realm*/
//        Realm iniRealm = new IniRealm("classpath:shiro.ini");

        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //收集用户身份和证明
        UsernamePasswordToken token = new UsernamePasswordToken("fxs", "123456");
        //提交身份和证明
        subject.login(token);

        //打印认证结果
        System.out.println("isAuthenticated:" + subject.isAuthenticated());

        //校验角色
        subject.checkRoles("admin", "user");

        //退出后 认证不会通过
        subject.logout();
        System.out.println("isAuthenticated:" + subject.isAuthenticated());
    }
}
