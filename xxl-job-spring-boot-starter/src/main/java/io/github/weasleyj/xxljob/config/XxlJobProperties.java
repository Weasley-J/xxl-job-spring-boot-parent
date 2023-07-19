package io.github.weasleyj.xxljob.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Xxl Job Properties
 *
 * @author weasley
 * @version 1.0.0
 */
@Data
@Validated
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {
    /**
     * 启用http server代理转发请求至xxl-job内置netty服务器
     * <p>
     * {@code enableProxy}为false的情况下job客户端必须开放{@code executor}的端口，否则 'xxl-job-admin' 无法回调客户端
     */
    private boolean enableProxy = true;
    /**
     * 执行器通讯TOKEN [选填]：非空时启用；
     */
    private String accessToken = "default_token";
    /**
     * 调度中心部署跟地址 [选填]：如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
     */
    @NotBlank(message = "调度中心地址不能为空")
    private String adminAddresses;
    /**
     * job执行器元数据
     */
    @NestedConfigurationProperty
    private JobExecutorProperties executor;

    /**
     * Job执行器元数据
     *
     * @implSpec Job客户端元数据
     */
    @Data
    public static class JobExecutorProperties {
        /**
         * 执行器注册 [选填]：优先使用该配置作为注册地址，为空时使用内嵌服务 ”IP:PORT“ 作为注册地址。从而更灵活的支持容器类型执行器动态IP和动态映射端口问题。
         */
        private String address;
        /**
         * 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
         */
        private String appName;
        /**
         * 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"
         */
        private String ip;
        /**
         * 内置netty执行器端口号（无需配置）
         *
         * @apiNote 不需要配置，程序自动启动会自动获取一个可用的tcp端口
         */
        private Integer port;
        /**
         * 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
         */
        private String logPath = "/data/applogs/xxl-job/jobhandler";
        /**
         * 执行器日志文件保存天数 [选填] ： 过期日志自动清理, 限制值大于等于3时生效; 否则, 如-1, 关闭自动清理功能；
         */
        private Integer logRetentionDays = 30;
    }
}
