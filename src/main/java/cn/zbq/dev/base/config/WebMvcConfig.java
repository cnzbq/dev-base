package cn.zbq.dev.base.config;

import cn.zbq.dev.base.auth.AuthInterceptor;
import cn.zbq.dev.base.propites.FileProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 应用配置
 *
 * @author zbq
 * @date 2020/8/14
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final FileProperties fileProperties;

    /**
     * 拦截器注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加权限验证拦截器，注意这里的Patterns不包括server.servlet.context-path配置的路径
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }

    /**
     * 处理请求跨域的问题
     * <p>
     * 也可以通过自定义一个CorsFilter的Bean实现
     * <pre>
     *     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
     *     CorsConfiguration config = new CorsConfiguration();
     *     config.setAllowCredentials(true);
     *     config.addAllowedOrigin("*");
     *     config.addAllowedHeader("*");
     *     config.addAllowedMethod("*");
     *     source.registerCorsConfiguration("/**", config);
     *     return new CorsFilter(source);
     * </pre>
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**");
    }

    /**
     * 配置静态文件映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pathUtl = "file:" + fileProperties.getTemplatesPath().replace("\\", "/");
        registry.addResourceHandler("/file/**").addResourceLocations(pathUtl).setCachePeriod(0);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
    }
}
