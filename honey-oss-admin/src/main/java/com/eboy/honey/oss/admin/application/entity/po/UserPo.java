package com.eboy.honey.oss.admin.application.entity.po;

import lombok.Data;

import java.util.Date;

/**
 * @author yangzhijie
 * @date 2020/10/14 11:00
 */
@Data
public class UserPo {

    private String userId;

    private String userName;

    private String userPassword;

    private String userPwSalt;

    private Date createTime;

    private Date updateTime;
}
