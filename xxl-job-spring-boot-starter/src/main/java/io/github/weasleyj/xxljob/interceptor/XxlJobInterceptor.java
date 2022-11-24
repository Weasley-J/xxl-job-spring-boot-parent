package io.github.weasleyj.xxljob.interceptor;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.xxl.job.core.biz.model.IdleBeatParam;
import com.xxl.job.core.biz.model.KillParam;
import com.xxl.job.core.biz.model.LogParam;
import com.xxl.job.core.biz.model.LogResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;
import com.xxl.job.core.server.EmbedServer;
import com.xxl.job.core.util.XxlJobRemotingUtil;
import io.github.weasleyj.xxljob.config.XxlJobProperties;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static com.xxl.job.core.util.XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN;

/**
 * 将<b>xxl-job-admin</b>的回调请求转发给<b>job服务</b>内部<b>xxl-job</b>内嵌的<b>Netty</b>服务器
 * <p>
 * 一个端口搞定, 避免服务器上一定要开放xxl-job执行器端口的白名单才能用接收<b>xxl-job-admin</b>的回调
 *
 * @author weasley
 * @version 1.0.0
 * @see com.xxl.job.core.biz.client.ExecutorBizClient
 * @see EmbedServer.EmbedHttpServerHandler#process(HttpMethod, String, String, String)
 */
@Slf4j
@Component
public class XxlJobInterceptor implements HandlerInterceptor {
    private final XxlJobProperties jobProperties;

    public XxlJobInterceptor(XxlJobProperties jobProperties) {
        this.jobProperties = jobProperties;
    }

    /**
     * HttpServletRequest -> JSON
     */
    public static String httpInputStreamToJsonString(HttpServletRequest request) throws IOException {
        return StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StringUtils.isNotBlank(request.getHeader(XXL_JOB_ACCESS_TOKEN))) {
            if (!jobProperties.isEnableProxy()) return true;

            String requestJson = httpInputStreamToJsonString(request);
            String nettyRequestUrl = "http://localhost:" + jobProperties.getExecutor().getPort() + request.getRequestURI();
            String uri = StringUtils.removeStart(request.getRequestURI(), "/");
            @SuppressWarnings({"all"}) ReturnT result = null;

            if ("beat".equals(uri)) {
                result = XxlJobRemotingUtil.postBody(nettyRequestUrl, request.getHeader(XXL_JOB_ACCESS_TOKEN), 1, "", String.class);
            }
            if ("idleBeat".equals(uri)) {
                IdleBeatParam idleBeatParam = JSONUtil.toBean(requestJson, new TypeReference<IdleBeatParam>() {
                }, true);
                result = XxlJobRemotingUtil.postBody(nettyRequestUrl, request.getHeader(XXL_JOB_ACCESS_TOKEN), 1, idleBeatParam, String.class);
            }
            if ("run".equals(uri)) {
                TriggerParam triggerParam = JSONUtil.toBean(requestJson, new TypeReference<TriggerParam>() {
                }, true);
                result = XxlJobRemotingUtil.postBody(nettyRequestUrl, request.getHeader(XXL_JOB_ACCESS_TOKEN), 1, triggerParam, String.class);
            }
            if ("kill".equals(uri)) {
                KillParam killParam = JSONUtil.toBean(requestJson, new TypeReference<KillParam>() {
                }, true);
                result = XxlJobRemotingUtil.postBody(nettyRequestUrl, request.getHeader(XXL_JOB_ACCESS_TOKEN), 1, killParam, String.class);
            }
            if ("log".equals(uri)) {
                LogParam logParam = JSONUtil.toBean(requestJson, new TypeReference<LogParam>() {
                }, true);
                result = XxlJobRemotingUtil.postBody(nettyRequestUrl, request.getHeader(XXL_JOB_ACCESS_TOKEN), 1, logParam, LogResult.class);
            }
            if (log.isDebugEnabled()) {
                log.debug("收到xxl-job的回调请求，uri：{}，入参：{}，执行结果：{}", request.getRequestURI(), requestJson, JSONUtil.toJsonStr(result));
            }
            if (null != result) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.println(JSONUtil.toJsonStr(result));
                writer.flush();
                writer.close();
                return false;
            }
        }
        return true;
    }
}
