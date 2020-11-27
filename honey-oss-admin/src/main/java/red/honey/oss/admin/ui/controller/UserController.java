package red.honey.oss.admin.ui.controller;

import com.eboy.honey.response.commmon.HoneyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import red.honey.oss.admin.application.annotation.AuthSkip;
import red.honey.oss.admin.application.service.UserService;

/**
 * @author yangzhijie
 * @date 2020/10/14 11:51
 */
@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @AuthSkip
    @PostMapping("/login")
    public HoneyResponse<String> login(String userId, String password) {
        return HoneyResponse.success(userService.login(userId, password));
    }
}
