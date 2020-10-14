package com.eboy.honey.oss.admin.application.service.impl;

import com.eboy.honey.oss.admin.application.constant.AppStateEnum;
import com.eboy.honey.oss.admin.application.dao.AppMapper;
import com.eboy.honey.oss.admin.application.entity.po.AppPo;
import com.eboy.honey.oss.admin.application.entity.vo.AppVo;
import com.eboy.honey.oss.admin.application.service.AppService;
import com.eboy.honey.oss.admin.application.utils.BeanConvertUtil;
import com.eboy.honey.oss.admin.application.utils.CommonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * @author yangzhijie
 * @date 2020/10/13 11:07
 */
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppMapper appMapper;

    /**
     * 根据应用Id获取应用信息
     *
     * @param appId 应用id
     * @return 应用信息
     */
    @Override
    public AppVo getAppInfoById(String appId) {
        Assert.notNull(appId, "appId must not null");
        return BeanConvertUtil.convert(appMapper.getAppInfoById(appId), AppVo.class);
    }

    /**
     * 分页获取应用信息
     *
     * @param pageNum  起始页
     * @param pageSize 页大小
     * @param orderBy  排序规则
     * @return 分页应用信息
     */
    @Override
    public PageInfo<AppVo> getApp(int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<AppPo> allApp = appMapper.getAllApp();
        PageInfo<AppPo> pageInfo = new PageInfo<>(allApp, pageNum);
        return BeanConvertUtil.pageInfoConvert(pageInfo, AppVo.class);
    }

    /**
     * 创建应用
     *
     * @param appVo 应用
     */
    @Override
    public void addApp(AppVo appVo) {
        appVo.setAppId(CommonUtil.get32Uid());
        appMapper.addApp(BeanConvertUtil.convert(appVo, AppPo.class));
    }

    /**
     * 根据应用id更新应用状态
     *
     * @param appId        应用id
     * @param appStateEnum 应用状态
     * @param reason       原由
     */
    @Override
    public void updateAppState(String appId, AppStateEnum appStateEnum, String reason) {
        Assert.notNull(appId, "appId must not null");
        AppVo app = Optional.of(getAppInfoById(appId)).orElseThrow(() -> new IllegalArgumentException("app not found"));
        if (app.getAppState() != appStateEnum.getCode() && appStateEnum.getCode() != AppStateEnum.NOT_ENABLE.getCode()) {
            appMapper.updateAppState(appId, appStateEnum, reason);
        }
    }

}
