package cn.zbq.dev.base.propites;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * token config properties
 *
 * @author Dingwq
 * @date 2020/8/21
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "authorization")
public class AuthorizationProperties {
    /**
     * jwt 密钥
     */
    private String jwtSecret;
    /**
     * token 有效时间，单位是分钟
     */
    private Integer effectiveTime = 2 * 60 * 60;
    /**
     * token发行者
     */
    private String issuer = "dev-base";
    /**
     * 保存用户id的key
     */
    private String userIdKey = "userId";
}
