package io.github.weasleyj.xxljob.config;

import io.github.weasleyj.xxljob.interceptor.XxlJobInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Xxl Job Web Mvc Config
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
@Configuration
public class XxlJobWebMvcConfig implements WebMvcConfigurer {

    private final XxlJobInterceptor xxlJobInterceptor;

    public XxlJobWebMvcConfig(XxlJobInterceptor xxlJobInterceptor) {
        this.xxlJobInterceptor = xxlJobInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(xxlJobInterceptor).addPathPatterns("/**");
    }
}
