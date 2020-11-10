package com.eboy.honey.oss.route;

import com.eboy.honey.oss.api.constant.CallbackEnum;
import com.eboy.honey.oss.task.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yangzhijie
 * @date 2020/11/10 11:14
 */
public class CallbackRoute {


    @Autowired
    private AsyncTask asyncTask;

    public void route(CallbackEnum callbackEnum) {

    }
}
