package com.fanxs.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author fanxs
 * @date 2021/9/15
 */
@Data
@ToString
public class User {
    String username;
    String password;
    Boolean rememberMe;
}
