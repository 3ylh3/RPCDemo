# rpcdemo
基于http传输协议以及fst序列化协议的rpc框架。
# 使用
引入jar包：

```xml
<dependency>
    <groupId>com.xiaobai.rpcdemo</groupId>
    <artifactId>core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
# 消费者端
服务消费者端在要调用的接口上添加@Remote注解(com.xiaobai.rpcdemo.core.consumer.annotation.Remote)：
```java
@RestController
public class Test {
    @Remote
    private TestService testService;

    @RequestMapping("/test")
    public String test() {
        return testService.test();
    }
}
```
配置文件中配置相关配置项：
```properties
#调用超时时间设置
rpcdemo.readTimeout=1000
rpcdemo.connectTimeout=1500
#nacos中注册的服务名，为空则默认为spring.application.name
rpcdemo.name=consumer-demo
#nacos地址
rpcdemo.nacosAddress=127.0.0.1:8848
```
# 提供者端
服务提供者端在对应接口的实现类上添加@Service注解(com.xiaobai.rpcdemo.core.provider.annotation.Service):
```java
@Service
public class TestImpl implements TestService {
    @Override
    public String test() {
        return "test";
    }
}
```
配置文件中配置相关配置项：
```properties
#nacos中注册的服务名，为空则默认为spring.application.name
rpcdemo.name=provider-demo
#nacos地址
rpcdemo.nacosAddress=127.0.0.1:8848
```
# 负载均衡策略
现阶段只支持随机调用

# jar包下载
https://github.com/3ylh3/RPCDemo/releases
