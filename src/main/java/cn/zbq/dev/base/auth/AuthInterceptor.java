package cn.zbq.dev.base.auth;

import cn.zbq.dev.base.annotations.AuthIgnore;
import cn.zbq.dev.base.utils.JwtTokenUtils;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 权限拦截
 * <p>
 * 请求时在header中增加Authorization字段
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

        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            throw new ApiException("授权已过期，请重新登录");
        }
        JwtTokenUtils.verifierToken(authorization);
        return true;
    }
}
