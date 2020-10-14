package com.eboy.honey.oss.admin.application.service.impl;

import com.eboy.honey.oss.admin.application.dao.UserMapper;
import com.eboy.honey.oss.admin.application.entity.po.UserPo;
import com.eboy.honey.oss.admin.application.entity.vo.UserVo;
import com.eboy.honey.oss.admin.application.service.UserService;
import com.eboy.honey.oss.admin.application.utils.BeanConvertUtil;
import com.eboy.honey.oss.admin.application.utils.CommonUtil;
import com.eboy.honey.oss.admin.application.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author yangzhijie
 * @date 2020/10/14 10:59
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户ids 查询用户列表
     *
     * @param userIds 用户ids
     * @return 用户列表
     */
    @Override
    public List<UserVo> findUserByIds(List<String> userIds) {
        List<UserPo> users = userMapper.findUserByIds(userIds);
        return BeanConvertUtil.convertByList(users, UserVo.class);
    }

    /**
     * 登录
     *
     * @param userId       用户id
     * @param userPassword 用户密码
     * @return 返回token
     */
    @Override
    public String login(String userId, String userPassword) {
        UserPo user = Optional.of(userMapper.findUserByIds(Collections.singletonList(userId))).orElseThrow(() -> new IllegalArgumentException("user not found")).get(0);
        String salt = user.getUserPwSalt();
        String credential = CommonUtil.createCredential(userPassword, salt);
        if (user.getUserPassword().equals(credential)) {
            return JwtUtils.sign(user);
        }
        throw new IllegalStateException("账号或密码错误");
    }
}
