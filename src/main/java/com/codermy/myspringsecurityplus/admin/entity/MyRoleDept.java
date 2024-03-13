package com.codermy.myspringsecurityplus.admin.entity;

import lombok.Data;

import java.io.Serializable;

/**
 *  *
 */
@Data
public class MyRoleDept implements Serializable {

    private static final long serialVersionUID = 8925514042332235875L;

    private Integer roleId;

    private Integer deptId;
}
