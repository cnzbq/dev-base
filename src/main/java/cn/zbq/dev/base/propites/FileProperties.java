package cn.zbq.dev.base.propites;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 有关文件的配置文件
 *
 * @author zbq
 * @date 2020/8/18
 */
@Data
@Component
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private String templatesPath;
}
