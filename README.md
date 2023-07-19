# xxl-job-spring-boot-starter

[![Maven Central](https://img.shields.io/maven-central/v/io.github.weasley-j/xxl-job-spring-boot-starter)](https://search.maven.org/artifact/io.github.weasley-j/xxl-job-spring-boot-starter)

一个用于简化`xxl-job`配置的`spring-boot-starter`组件, 使用`job客户端`的`http server port`
来接收[xxl-job-admin](https://github.com/xuxueli/xxl-job/)的任务调度请求，不需要在`job客户端`
服务器上额外开通`xxl-job-core`内置的`netty服务器`的端口(`xxl.job.executor.port`)，`Job`客户端的服务器不需要开放`xxl-job`
执行器的端口的安全组策略就能完成调度，增加开关控制(`xxl.job.enable-proxy`)，默认开启，组件不改变原`xxl-job`的任何特性。

## 1 添加pom依赖

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
# xxj-job配置
xxl:
  job:
    access-token: default_token
    admin-addresses: http://ip:port/xxl-job-admin
    # Job客户端元数据
    executor:
      app-name: xxl-job-spring-boot-starter-tests
      address: http://fgimax2.fgnwctvip.com:${server.port}
      log-path: ./logs
```

提示：同一台机器模拟`Job客户端`集群的时候注意`executor`的端口偏移

## 3 版本适配

| 项            | 版本            | 备注 |
|--------------|---------------|----|
| JDK          | 1.8+          |    |
| xxl-job-core | 2.x.x         |    |
| springboot   | 2.x.x - 3.x.x |    |

