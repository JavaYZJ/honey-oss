package com.eboy.honey.oss.admin.application.dao;

import com.eboy.honey.oss.admin.application.entity.po.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/10/14 11:05
 */
@Mapper
public interface UserMapper {

    /**
     * 登录
     *
     * @param userId       用户id
     * @param userPassword 用户密码
     */
    void login(@Param("userId") String userId, @Param("userPassword") String userPassword);

    /**
     * 根据用户ids查询用户列表
     *
     * @param userIds 用户ids
     * @return 用户列表
     */
    List<UserPo> findUserByIds(@Param("userIds") List<String> userIds);
}
