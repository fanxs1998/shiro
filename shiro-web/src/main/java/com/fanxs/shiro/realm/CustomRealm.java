package com.fanxs.shiro.realm;

import com.fanxs.dao.UserDao;
import com.fanxs.vo.User;
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

import javax.annotation.Resource;
import java.util.*;

/**
 * @author fanxs
 * @date 2021/9/14
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;


    //用于授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        //从数据库/缓存中获取角色数据
        Set<String> roles = getRolesByUserName(username);
        Set<String> permissions = getPermissionsByUserName(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    //获取角色权限
    private Set<String> getPermissionsByUserName(String username) {
        Set<String> sets = new HashSet<String>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    //获取角色数据
    private Set<String> getRolesByUserName(String username) {
        System.out.println("从数据库中获取授权数据...");
        List<String> list = userDao.queryRolesByUsername(username);
        Set<String> sets = new HashSet<>(list);
        return sets;
    }

    //用于认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.从主体传入的认证信息中，获取用户名
        String username = (String) authenticationToken.getPrincipal();

        //2.通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(username);
        if (password == null){
            return null;
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                username, password, getName());
        String name = getName();
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username));//加盐修饰
        return simpleAuthenticationInfo;
    }

    private String getPasswordByUserName(String username) {
        User user = userDao.getUserByUsername(username);
        if (user != null){
            return user.getPassword();
        }

        return null;
    }


    /**
     * 生成盐值加密后的密码
     * @param args
     */
    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456","fxs");
        System.out.println(md5Hash.toString());
    }
}
