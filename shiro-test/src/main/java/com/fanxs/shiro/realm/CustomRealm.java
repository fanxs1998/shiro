package com.fanxs.shiro.realm;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author fanxs
 * @date 2021/9/14
 */
public class CustomRealm extends AuthorizingRealm {

    Map<String,String> userMap = new HashMap<>(16);
    {
        userMap.put("fxs", "65e9ae981b087b1ce5cea68c9924a119");
        userMap.put("dzq", "654321");

        super.setName("customRealm");
    }

    /**
     * 授权
     * @param principals
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String username = (String)principals.getPrimaryPrincipal();
        // 从数据库或者缓存获取角色数据和权限数据
        Set<String> roles = getRolesByUsername(username);
        Set<String> permissions = getPermissionsByUsername(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setStringPermissions(permissions);
        authorizationInfo.setRoles(roles);

        return authorizationInfo;
    }

    /**
     * 模拟从数据库或者缓存获取权限信息
     * @param username
     * @return
     */
    private Set<String> getPermissionsByUsername(String username) {
        Set<String> sets = new HashSet<>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    /**
     * 模拟从数据库或缓存获取角色信息
     * @param username
     * @return
     */
    private Set<String> getRolesByUsername(String username) {
        Set<String> sets = new HashSet<>();
        sets.add("admin");
        sets.add("user");
        return sets;
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 1.从主体传过来的认证信息中，获得用户名
        String username = (String)token.getPrincipal();
        // 2.通过用户名从数据库获取凭证
        String password = getPasswordByUsername(username);
        if(password == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("fxs", password, "customRealm");

        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("qwer!@#$"));//盐值

        return authenticationInfo;
    }

    /**
     * 模拟数据库访问凭证
     * @param username
     * @return
     */
    private String getPasswordByUsername(String username) {

        return userMap.get(username);
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456","qwer!@#$");
        System.out.println(md5Hash);
    }
}
