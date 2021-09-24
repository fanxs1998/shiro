package com.fanxs.dao;

import com.fanxs.vo.User;

import java.util.List;

/**
 * @author fanxs
 * @date 2021/9/15
 */
public interface UserDao {

    /**
     * 通过用户名查询用户信息
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 通过用户名查询角色
     * @param username
     * @return
     */
    List<String> queryRolesByUsername(String username);
}
