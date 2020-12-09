package red.honey.oss.admin.application.service;

import red.honey.oss.admin.application.entity.vo.UserVo;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/10/14 10:41
 */
public interface UserService {

    /**
     * 根据用户ids 查询用户列表
     *
     * @param userIds 用户ids
     * @return 用户列表
     */
    List<UserVo> findUserByIds(List<String> userIds);

    /**
     * 登录
     *
     * @param userId       用户id
     * @param userPassword 用户密码
     * @return 返回token
     */
    String login(String userId, String userPassword);
}
