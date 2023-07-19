package com.example.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * xxl job demo
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
@Component
public class XxlJobDemo {

    @XxlJob("demoJobHandler")
    public ReturnT<String> demoJobHandler(String args) throws Exception {
        XxlJobHelper.log("这是一个xxl-job的测试程序 job入参 {}", args);
        log.info("这是一个xxl-job的测试程序 job入参 {}", args);
        ReturnT<String> success = ReturnT.SUCCESS;
        success.setContent("你好：这是一个 xxl-job 增强的示例！");
        return success;
    }
}
