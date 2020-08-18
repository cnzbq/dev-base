package cn.zbq.dev.base.annotations;

import java.lang.annotation.*;

/**
 * api 鉴权忽略注解
 * <p>
 * 目前该注解仅作用于方法级别，后期可能会考虑作用于类级别
 * 鉴权忽略{@link cn.zbq.dev.base.auth.AuthInterceptor}
 * api文档生成分组{@link cn.zbq.dev.base.config.Swagger2Config#createPublicApi}
 *
 * @author zbq
 * @date 2020/8/13
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthIgnore {
}
