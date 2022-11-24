package io.github.weasleyj.xxljob.config;


import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl job config
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({XxlJobProperties.class})
public class XxlJobConfiguration {

    /**
     * @return XxlJobSpringExecutor
     */
    @Bean
    @ConditionalOnMissingBean({XxlJobSpringExecutor.class})
    public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties jobProperties) {
        if (log.isInfoEnabled()) {
            log.info("Initializing xxl-job config properties.");
        }
        XxlJobProperties.ExecutorProperties executor = jobProperties.getExecutor();
        XxlJobSpringExecutor jse = new XxlJobSpringExecutor();
        jse.setAdminAddresses(jobProperties.getAdminAddresses());
        jse.setAccessToken(jobProperties.getAccessToken());
        jse.setAppname(executor.getAppName());
        jse.setAddress(executor.getAddress());
        jse.setIp(executor.getIp());
        jse.setPort(executor.getPort());
        jse.setLogPath(executor.getLogPath());
        jse.setLogRetentionDays(executor.getLogRetentionDays());
        return jse;
    }

}
