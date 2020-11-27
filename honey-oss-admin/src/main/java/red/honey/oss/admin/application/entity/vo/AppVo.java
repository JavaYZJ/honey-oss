package red.honey.oss.admin.application.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author yangzhijie
 * @date 2020/10/13 10:45
 */
@Data
public class AppVo {

    private String appId;

    private String appName;

    private int appState;

    private String stateReason;

    private Date createTime;

    private Date updateTime;
}
