## 自己搭建一套开发的基础框架

### 一些想法
管理端的框架已经很多了，但是适用于为移动端开发api接口的框架还是比较少的
于是决定自己从头好好搭建一个
加油！！！

|  已集成框架    | 版本号  |
|  ----        | ----   |
| SpringBoot   | 2.3.3  |
| Mybatis      | 3.5.5  |
| Mybatis-Plus | 3.4.0  |
| Redisson     | 3.13.3 |
| springfox    | 3.0.0  |
| Swagger2     | 2.1.2  |
| MySQL        | 8.x    |
| auth0/jwt    | 3.10.3 |


- 2020-08-25
集成Redisson，官方文档 `https://github.com/redisson/redisson/tree/master/redisson-spring-boot-starter#spring-boot-starter`
可以RedissonClient/RedisTemplate/ReactiveRedisTemplate来使用

- 2020-08-16
集成Swagger2，并对需要授权和不需要授权的接口进行了分组展示
增加全局异常处理
增加自定义鉴权注解 `@AuthIgnore`

- 2020-08-13
目前日志框架使用的是logback，后期准备更换为log4j2

- 2020-08-05
基于SpringBoot 2.3.3     
集成Mybatis-Plus和代码生成器