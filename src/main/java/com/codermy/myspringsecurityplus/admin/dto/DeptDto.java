package com.codermy.myspringsecurityplus.admin.dto;

import com.codermy.myspringsecurityplus.admin.entity.BaseEntity;
import lombok.Data;

/**
 *  *
 */
@Data
public class DeptDto extends BaseEntity {

    private Integer id;

    private Integer parentId;

    private String checkArr = "0";

    private String title;
}
