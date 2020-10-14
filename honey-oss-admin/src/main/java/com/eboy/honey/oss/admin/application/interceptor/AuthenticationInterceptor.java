package com.eboy.honey.oss.admin.application.interceptor;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.eboy.honey.oss.admin.application.annotation.AuthSkip;
import com.eboy.honey.oss.admin.application.constant.JWTConstant;
import com.eboy.honey.oss.admin.application.entity.vo.UserVo;
import com.eboy.honey.oss.admin.application.service.UserService;
import com.eboy.honey.response.constant.HoneyUCExceptionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;


/**
 * @author yangzhijie
 * @date 2020/7/14 16:13
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        // 检查是否有authSkip注释，有则跳过认证
        if (method.isAnnotationPresent(AuthSkip.class)) {
            AuthSkip authSkip = method.getAnnotation(AuthSkip.class);
            if (authSkip.required()) {
                return true;
            }
        }
        // 执行认证
        HoneyUCExceptionException.TOKEN_NOT_FOUND.assertNotNull(token);
        // 获取 token 中的 user id
        String userId = "";
        try {
            userId = JWT.decode(token).getClaim("userId").asString();
        } catch (JWTDecodeException j) {
            HoneyUCExceptionException.INVALID_TOKEN_NOT_HAVE_USER_ID_CLAIM.throwException();
        }
        Optional<UserVo> user = userService.findUserByIds(Collections.singletonList(userId)).stream().findFirst();
        if (!user.isPresent()) {
            HoneyUCExceptionException.USER_NOT_FOUND.throwException();
        }
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(JWTConstant.SECRET)).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            HoneyUCExceptionException.INVALID_TOKEN.throwException();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}

