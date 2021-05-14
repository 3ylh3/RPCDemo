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
@Remote注解可以指定调用的服务提供者名(rpcdemo.name)以及组（通过providerName以及group指定）,如果未指定则从所有服务提供者以及所有组中选择：
```java
@Remote(providerName = "test", group = "test")
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
@Service注解可以指定服务所属组（通过group指定）：
```java
@Service(group = "test")
```
配置文件中配置相关配置项：
```properties
#nacos中注册的服务名，为空则默认为spring.application.name
rpcdemo.name=provider-demo
#nacos地址
rpcdemo.nacosAddress=127.0.0.1:8848
```
# 负载均衡策略
支持轮询、随机以及按权重选择三种策略，默认为轮询。
消费者配置负载均衡策略：
```java
@Remote(loadBlance = LoadBalanceEnum.WEIGHT)
```
其中：
LoadBalanceEnum.ROUND:轮询
LoadBalanceEnum.RANDOM:随机
LoadBalanceEnum.WEIGHT:按权重选择
提供者设置权重：
```java
@Service(weight = 10)
```
# jar包下载
https://github.com/3ylh3/RPCDemo/releases

# 控制台使用
克隆web模块后修改配置文件中端口和nacos地址，打包启动即可访问控制台。
