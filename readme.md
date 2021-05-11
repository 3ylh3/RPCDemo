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
# 负载均衡策略
现阶段只支持随机调用

# jar包下载
https://github.com/3ylh3/RPCDemo/releases
