package cn.zbq.dev.base.auth;

import cn.zbq.dev.base.annotations.AuthIgnore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 权限拦截
 *
 * @author zbq
 * @date 2020/8/14
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthIgnore authIgnore;

        // 判断是否为mvc处理方法
        if (handler instanceof HandlerMethod) {
            authIgnore = ((HandlerMethod) handler).getMethodAnnotation(AuthIgnore.class);
        } else {
            return true;
        }

        // 判断是否包含权限忽略注解
        if (Objects.nonNull(authIgnore)) {
            return true;
        }

        // todo 验证token是否有效的相关逻辑
        System.out.println();

        //throw new ApiException("测试一下");
        return true;
    }
}
