package com.codermy.myspringsecurityplus.admin.service;

import com.codermy.myspringsecurityplus.admin.entity.MyRoleUser;

import java.util.List;

/**
 *  * 
 */
public interface RoleUserService {
    /**
     * 返回用户拥有的角色
     * @param userId
     * @return
     */
    List<MyRoleUser> getMyRoleUserByUserId(Integer userId);
}
