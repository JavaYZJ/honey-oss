package com.eboy.honey.oss.admin.application.dao;


import com.eboy.honey.oss.admin.application.constant.AppStateEnum;
import com.eboy.honey.oss.admin.application.entity.po.AppPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/10/13 9:58
 */
@Mapper
public interface AppMapper {

    /**
     * 根据id查询应用信息
     *
     * @param appId 应用id
     * @return 应用信息
     */
    AppPo getAppInfoById(@Param("appId") String appId);

    /**
     * 获取所有应用
     *
     * @return 所有应用信息
     */
    List<AppPo> getAllApp();


    /**
     * 创建应用
     *
     * @param appPo 应用
     */
    void addApp(@Param("appPo") AppPo appPo);

    /**
     * 查询所有应用条数
     *
     * @return 所有应用的条数
     */
    long countApp();

    /**
     * 更新应用状态
     *
     * @param appId        应用id
     * @param appStateEnum 应用状态
     * @param reason       原由
     */
    void updateAppState(@Param("appId") String appId, @Param("appStateEnum") AppStateEnum appStateEnum, @Param("reason") String reason);


}
