package red.honey.oss.route;

import org.springframework.beans.factory.annotation.Autowired;
import red.honey.oss.api.constant.CallbackEnum;
import red.honey.oss.task.AsyncTask;

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
