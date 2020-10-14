package com.eboy.honey.oss.server.api.web.controller;

import com.eboy.honey.oss.server.application.constant.AppStateEnum;
import com.eboy.honey.oss.server.application.service.AppService;
import com.eboy.honey.oss.server.application.vo.AppVo;
import com.eboy.honey.response.commmon.HoneyResponse;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

/**
 * @author yangzhijie
 * @date 2020/10/13 11:36
 */
@RestController
@RequestMapping("/v1/app")
public class AppController {

    @Autowired
    private AppService appService;

    /**
     * 分页获取应用
     */
    @GetMapping
    public HoneyResponse<PageInfo<AppVo>> getApp(@PageableDefault(sort = {"update_time"}, direction = Sort.Direction.DESC) Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortBy = pageable.getSort().toString().replace(":", " ");
        return HoneyResponse.success(appService.getApp(pageNumber, pageSize, sortBy));
    }

    /**
     * 根据应用id获取应用信息
     */
    @GetMapping("/{appId}")
    public HoneyResponse<AppVo> getAppById(@PathVariable String appId) {
        return HoneyResponse.success(appService.getAppInfoById(appId));
    }

    /**
     * 更新应用状态
     */
    @PutMapping("/{appId}")
    public HoneyResponse updateAppState(@PathVariable String appId,
                                        @RequestParam int appState,
                                        @RequestParam(required = false) String reason) {
        appService.updateAppState(appId, AppStateEnum.getByCode(appState), reason);
        return HoneyResponse.success();
    }

    @PostMapping
    public HoneyResponse addApp(@RequestBody AppVo appVo) {
        appService.addApp(appVo);
        return HoneyResponse.success();
    }
}
