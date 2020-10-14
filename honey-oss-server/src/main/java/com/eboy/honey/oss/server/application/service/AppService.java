package com.eboy.honey.oss.server.application.service;

import com.eboy.honey.oss.server.application.constant.AppStateEnum;
import com.eboy.honey.oss.server.application.vo.AppVo;
import com.github.pagehelper.PageInfo;

/**
 * @author yangzhijie
 * @date 2020/10/13 10:45
 */
public interface AppService {

    /**
     * 根据应用Id获取应用信息
     *
     * @param appId 应用id
     * @return 应用信息
     */
    AppVo getAppInfoById(String appId);

    /**
     * 分页获取应用信息
     *
     * @param pageNum  起始页
     * @param pageSize 页大小
     * @param orderBy  排序规则
     * @return 分页应用信息
     */
    PageInfo<AppVo> getApp(int pageNum, int pageSize, String orderBy);

    /**
     * 创建应用
     *
     * @param appVo 应用
     */
    void addApp(AppVo appVo);

    /**
     * 根据应用id更新应用状态
     *
     * @param appId        应用id
     * @param appStateEnum 应用状态
     * @param reason       原由
     */
    void updateAppState(String appId, AppStateEnum appStateEnum, String reason);

}
