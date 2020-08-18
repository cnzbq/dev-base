package cn.zbq.dev.base.config;

import cn.zbq.dev.base.annotations.AuthIgnore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Swagger2 config
 *
 * @author zbq
 * @date 2020/8/16
 */
@Configuration
public class Swagger2Config {

    @Bean(value = "defaultApi")
    public Docket createDefaultApi() {
        RequestParameterBuilder parameterBuilder = new RequestParameterBuilder();
        RequestParameter parameter = parameterBuilder
                .name("token")
                .description("访问令牌")
                .in(ParameterType.HEADER)
                .required(true)
                .build();
        List<RequestParameter> globalRequestParameters = new ArrayList<>(1);
        globalRequestParameters.add(parameter);

        // 当需要匹配多个包的controller时通过or拼接即可，本质是实现一个Predicate的判断函数
        Predicate<RequestHandler> packagePredicate = RequestHandlerSelectors.basePackage("cn.zbq.dev.base.work.controller")
                // .or(RequestHandlerSelectors.basePackage("cn.zbq.dev.base.test.controller"))
                .and(RequestHandlerSelectors.withMethodAnnotation(AuthIgnore.class).negate());

        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(packagePredicate)
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(globalRequestParameters)
                .groupName("需要token验证")
                .ignoredParameterTypes(HttpServletResponse.class, HttpServletRequest.class);
    }

    @Bean(value = "publicApi")
    public Docket createPublicApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                // 只扫描含有 @AuthIgnore 注解的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(AuthIgnore.class))
                .paths(PathSelectors.any())
                .build()
                .groupName("无需token验证")
                .ignoredParameterTypes(HttpServletResponse.class, HttpServletRequest.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("springboot利用swagger2构建api文档")
                .description("restful")
                .termsOfServiceUrl("这里是提供服务的网址")
                .version("1.0")
                .build();
    }
}
