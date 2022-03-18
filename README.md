# open_blogV1.0项目简介
open_blog是为hexo静态博客提供一个编写、发布md文档的后台服务。

有感于每次写新的博客都需要重新执行hexo命令， 并且不支持编写修改文档等原因，故此开发此项。
本项主要还是辅助hexo，完成自动化发布生成等操作。


## 快速开始

1.git clone https://github.com/authorZhao/blog-backend.git

2.环境安装 node、hexo、mysql8.0、nginx等 [hexo使用](http://www.pingyuanren.top/2022/03/19/hexo%E9%9D%99%E6%80%81%E5%8D%9A%E5%AE%A2%E6%90%AD%E5%BB%BA(%E4%B8%80)/)

3.导入项目sql open_blog.sql

4.导入idea 直接运行  jdk版本>=1.8

5.启动客户端页面 访问页面

## 项目结构

blog_core 基础包

blog_boot 项目模块，主要是提供api的模块

## 项目原理简述
1.hex自动发布的原理是基于命令行操作的，也就是说，服务器也需要安装hexo相关依赖 


## 项目设计技术


|  技术点    |    作用  |   版本   |
| ---- | ---- | ---- |
|   mysql   |   数据库*必须*   |   8.0   |
|   redis   |    缓存等*非必须*  |    5.0  |
|   node  |   执行hexo命令*必须*   |   14.0   |


### 项目配置：

前端博客页面域名 xxx
前端管理后台 xxx
后端域名 xxx

