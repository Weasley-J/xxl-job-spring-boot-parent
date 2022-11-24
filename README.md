# xxl-job-spring-boot-starter

[![Maven Central](https://img.shields.io/maven-central/v/io.github.weasley-j/xxl-job-spring-boot-starter)](https://search.maven.org/artifact/io.github.weasley-j/xxl-job-spring-boot-starter)

一个用于简化`xxl-job`配置的`spring-boot`组件, 使用`job客户端`的`http server port`
就能接收来自[xxl-job-admin](https://github.com/xuxueli/xxl-job/)的调度请求，不需要在`job客户端`
服务器上额外开通`xxl-job-core`内置的`netty服务器`的端口(`xxl.job.executor.port`)
就能完成调度，增加开关控制(`xxl.job.enable-proxy`)，默认开启，本组件不改变原包任何功能特性。

## 1 添加pom坐标

版本号在`maven`中央仓库获取

```xml

<dependency>
    <groupId>io.github.weasley-j</groupId>
    <artifactId>xxl-job-spring-boot-starter</artifactId>
    <version>${xxl-job-starter.verison}</version>
</dependency>
```

## 2 配置项目的`yaml`文件

> IDEA辅助配置元数据提示

```yaml
# job配置
xxl:
  job:
    access-token: default_token
    admin-addresses: http://ip:port/xxl-job-admin
    executor:
      app-name: xxl-job-spring-boot-starter-tests
      address: http://fgimax2.fgnwctvip.com:${server.port}
      #Job客户端的服务器不需要开放执行器的端口的安全组策略，xxl-job-spring-boot-starter会转发到Job客户端内嵌的Netty服务器里面并实时响应给xxl-job-admin
      port: 9999
      log-path: ./logs
```

提示：同一台机器模拟`Job客户端`集群的时候注意`executor`的端口偏移

## 3 版本要求

| 项           | 版本  | 备注 |
| ------------ | ----- | ---- |
| JDK          | 1.8+  |      |
| xxl-job-core | 2.x.x |      |
| springboot   | 2.x.x |      |

