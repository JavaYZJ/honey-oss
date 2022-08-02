# 欢迎使用 Honey-OSS 分布式存储中间件

------

> 官方文档：**http://www.honey.red**

**Honey-OSS** 是基于Minio开发的分布式存储中间件。具备常规文件功能上传下载的同时具备断点（分片）上传、断点下载、秒传、对象资源鉴黄鉴暴等功能。
**Honey-OSS**支持公有、私有两种部署方式，再通过**SDK**进行OSS操作。公有部署后则可向全网开发者开放使用。私有化部署则面向企业内部OSS服务自建、自用。
以下是特点总结：

> * SDK 开箱即用
> * 支持断点（分片）上传、异步上传、断点下载、秒传、对象资源鉴黄鉴暴等
> * 图片操作时，提供缩略图配套操作
> * 良好的扩展机制，方便开发者扩展秒传、回调策略等

备注：Honey-OSS-SDK 目前仅支持Java且必须在Spring环境下

> * 源码地址：[https://github.com/JavaYZJ/honey-oss][1]
> * 欢迎给位兴趣爱好者加入进来，共同维护。当然，如果你觉得这项目可以，请点个**star**哦。


------
# Honey-OSS架构

>  Honey-OSS 技术栈主要是SpringBoot、zookeeper、dubbo、minio、mybatis、mysql。其架构图示意如下：

![](http://oss.honey.red/public/honey-oss%E6%9E%B6%E6%9E%84%E5%9B%BE.png "Honey-OSS架构")

------
架构总体来说还算简单，没有过多复杂设计。相信熟悉Java开发的看到此架构应不会陌生。采用zookeeper作为注册中心，实现服务的注册与发现，**honey-oss-server**作为honey-oss整个服务的核心模块，向外即可提供REST服务，同时也将服务通过dubbo向ZK注册。**honey-oss-sdk**作为工具SDK方便开发者使用。另外**honey-oss-api**可方便开发者二次开发封装，打造更更方便强大的功能。

> 下个版本会激活**honey-oss-admin**模块，实现接入方的管控

------


  [1]: https://github.com/JavaYZJ/honey-oss
