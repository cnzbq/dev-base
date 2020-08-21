package cn.zbq.dev.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * spring bean utils
 *
 * @author Dingwq
 * @date 2020/8/21
 */
@Slf4j
@Component
public class SpringBeanUtils implements ApplicationContextAware, DisposableBean {
    private static ApplicationContext context;

    public static <T> T getBean(Class<T> requiredType) {
        check();
        return context.getBean(requiredType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        check();
        return (T) context.getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("注入spring上下文");
        SpringBeanUtils.context = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        log.info("销毁spring上下文");
        SpringBeanUtils.context = null;
    }

    private static void check() {
        if (Objects.isNull(context)) {
            log.error("ApplicationContext 尚未注册");
            throw new IllegalArgumentException("ApplicationContext 尚未注册");
        }
    }

}
