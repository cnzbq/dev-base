package cn.zbq.dev.base.propites;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * thread pool config properties
 *
 * @author Dingwq
 * @date 2020/9/1
 */
@Data
@Component
@ConfigurationProperties("thread.pool")
public class AppThreadPoolProperties {
    //核心线程池大小
    private final int corePoolSize = 10;
    //最大线程数
    private final int maxPoolSize = 15;
    //队列容量
    private final int queueCapacity = 200;
    //活跃时间/秒
    private final int keepAliveSeconds = 60;
}
