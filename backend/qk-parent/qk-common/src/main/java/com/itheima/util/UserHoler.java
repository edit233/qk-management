package com.itheima.util;

/**
 * 操作当前登录用户信息
 */
public class UserHoler {

    // 当前登录用户
    private static ThreadLocal<Integer> CURRENT_USER = new ThreadLocal<>();

    // 设置当前登录用户
    public static void setCurrentUser(Integer userId) {
        CURRENT_USER.set(userId);
    }

    // 获取当前登录用户
    public static Integer getCurrentUser() {
        return CURRENT_USER.get();
    }

    // 移除当前登录用户
    public static void removeCurrentUser() {
        CURRENT_USER.remove();
    }
}