

## 第一章  环境与概念

* 1.1  开篇介绍
    * 1.1.1  本书特点
    * 1.1.2  本书侧重点
    * 1.1.3  环境
    * 1.1.4  配套示例项目
    * 1.1.5  前置课程
    * 1.1.6  勘误
    * 1.1.7  资料汇总
* 1.2  Android App 构建与架构
    * 1.2.1  Gradle 与 Android Gradle Plugin (AGP)
    * 1.2.2  构建对架构的影响
        * 提高研发团队的效能
        * 扩展运行时方案的可能性
* 1.3  构建工具发展历史
    * 1.3.1  Ant 和 Maven
    * 1.3.2  Gradle
    * 1.3.3  Bazel 和 Buck
    * 1.3.4  为什么 Android 现在还用 Gradle
* 1.4  手动构建一个 App
    * 1.4.1  材料准备
    * 1.4.2  资源构建
    * 1.4.3  源码构建
    * 1.4.4  APK 打包
    * 1.4.5  拓展思考：.aar 和 .aab 格式的探索
* 1.5  Run 按钮背后
    * 1.5.1  Run 按钮与命令定制
    * 1.5.2  尝试 CLI 命令执行
    * 1.5.3  Gradle 命令面板
    * 1.5.4  如何知道任务的执行内容？
    * 1.5.5  如何着手自定义任务/插件？


## 第二章  快速上手

* 2.1  Gradle 项目的工程结构
    * 2.1.1  build.gradle(.kts)/settings.gradle(.kts)
    * 2.1.2  构建源码模块（buildSrc）
    * 2.1.3  复合构建（Composite Build）
    * 2.1.4  解耦多插件的配置
* 2.2  Gradle 生命周期基础梳理
    * 2.2.1  生命周期全览
        * 2.2.1.1  初始化阶段（Initialization）
        * 2.2.1.2  配置阶段（Configuration）
        * 2.2.1.3  执行阶段（Execution）
    * 2.2.2  上下文对象
        * 2.2.2.1  Gradle
        * 2.2.2.2  Settings
        * 2.2.2.3  Project
* 2.3  Kotlin 和 Gradle Kotlin DSL
    * 2.3.1  使用 Kotlin 编写工具
    * 2.3.2  泛用型 KTS 脚本
    * 2.3.3  Gradle Kotlin DSL
    * 2.3.4  Safe Accessor 扩展查找
    * 2.3.5  构建脚本整合策略
    * 2.3.6  现有插件对 Kotlin DSL 的友好支持
* 2.4  第一个插件：发送构建通知到 Slack
    * 2.4.1  需求分析和材料准备
    * 2.4.2  初始化插件入口
    * 2.4.3  编写插件扩展
    * 2.4.4  编写任务
    * 2.4.5  组装所有部件
    * 2.4.6  配置并运行
    * 2.4.7  抽象类/成员变量的使用
* 2.5  Gradle 插件分类
    * 2.5.1  根据插件载体分类
        * 2.5.1.1  脚本插件（Script Plugin）
        * 2.5.1.2  二进制插件（Binary Plugin）
        * 2.5.1.3  功能导出
    * 2.5.2  根据插件功能分类
        * 2.5.2.1  约定插件（Convention Plugin）
        * 2.5.2.2  多功能插件（Multi-Function Plugin）
* 2.6  Gradle 任务基础梳理
    * 2.6.1  任务的分类
    * 2.6.2  任务的输入输出
    * 2.6.3  任务的状态
* 2.7  源码与调试
    * 2.7.1  Gradle
    * 2.7.2  Android Gradle Plugin（AGP）
    * 2.7.3  AAPT2、D8&R8
    * 2.7.4  Annotation Processor / KAPT / KSP
    * 2.7.5  自定义脚本、插件


## 第三章 扩展 Android 构建流程

* 3.1  变体（Variant）
    * 3.1.1  Gradle Module Metadata 的变体（Variant）
    * 3.1.2  Android Gradle Plugin (AGP) 的变体（Variant）
    * 3.1.3  AGP Variant API 与可感知变体的任务（Variant-Aware Task）
* 3.2  Variant&Artifact API v1
    * 3.2.1  Variant - 获取已配置内容
    * 3.2.2  Variant - 二次配置
    * 3.2.3  Variant - 禁用组合
    * 3.2.4  Artifact - 获取工件
    * 3.2.5  为生态协同插件编写任务的两要素
    * 3.2.6  API v1 的痛点：非公开 API
* 3.3  Variant&Artifact API v2
    * 3.3.1  AGP 新的分包策略
    * 3.3.2  Variant - 获取已配置内容
    * 3.3.3  Variant - 二次配置
    * 3.3.4  Variant - 新生命周期
    * 3.3.5  Artifact：获取工件
    * 3.3.6  Artifact：创建、变换、追加
    * 3.3.7  API v2 的改进和未来展望
* 3.4  溯源 AGP 入口流程
    * 3.4.1 “提出问题-源码分析-解决方案”的思考流程
    * 3.4.2 AppPlugin 入口
    * 3.4.3 AGP 项目配置
    * 3.4.4 创建 Extension
    * 3.4.5 创建 Task
* 3.5  溯源 Artifact API
    * 3.5.1  公开的 Artifact API 探索
    * 3.5.2  内部的 Artifact API 探索
* 3.6  创建自己的 Artifact 集合 - Polyfill 工具库
    * 3.6.1  Polyfill 架构设计目标
    * 3.6.2  与 AGP 统一的 API 风格
    * 3.6.3  与 AGP 生命周期绑定
    * 3.6.4  逻辑承载容器：Task 和 PolyfillAction
    * 3.6.5  向后兼容：支持多个 AGP 版本
    * 3.6.6  集成 Polyfill


## 第四章  深入 Gradle 原生 API

* 4.1  生命周期的钩子（Hook）
    * 4.1.1  Gradle 的钩子
    * 4.1.2  Project 的钩子
    * 4.1.3  TaskGraph 的钩子
    * 4.1.4  BuildService
    * 4.1.5  实战演示：打印各阶段耗时
* 4.2  插件扩展的属性/任务的属性
    * 4.2.1  原始类型
    * 4.2.2  惰性类型 Provider 和 Property
    * 4.2.3  文件类型及其 Property
    * 4.2.4  其他 Property
    * 4.2.5  Property 的限制
    * 4.2.6  实战演示：任务运行阶段确定的入参
* 4.3  DSL 嵌套
    * 4.3.1  容器对象配置：NamedDomainObjectContainer
    * 4.3.2  单一对象配置：Action
    * 4.3.3  Lambda/Closure 闭包传递
    * 4.3.4  实战演示：多频道通知 + DefaultConfig + Variant 过滤
    * 4.3.5  扩展思考
* 4.4  任务编排
    * 4.4.1  由任务名查实现类
    * 4.4.2  单任务内部逻辑编排
    * 4.4.3  多任务编排
    * 4.4.4  扩展思考
* 4.5  缓存与增量机制
    * 4.5.1  配置阶段缓存（Configuration Cache）
    * 4.5.2  执行阶段的缓存：增量构建（Incremental Build）与最新版检测（UP-TO-DATE Check）
    * 4.5.3  二级缓存系统：构建缓存（Build Cache）
    * 4.5.4  实战演示：编写符合增量与缓存规范的插件
* 4.6  插件测试
    * 4.6.1  测试分类
    * 4.6.2  测试替身（Test Doubles）
    * 4.6.3  各 Gradle 插件类型的测试侧重点
    * 4.6.4  Gradle 测试套件
    * 4.6.5  实战演示：为插件添加测试代码


## 第五章  资源构建扩展

* 5.1  AGP 资源交互 API 的进阶使用
    * 5.1.1  添加资源 SourceSet
    * 5.1.2  AndroidManifest.xml 占位与合并
    * 5.1.3  常规 xml 键值对插入
    * 5.1.4  为 APK/AAR 添加额外资源文件
    * 5.1.5  为 APK/AAR 移除资源文件
    * 5.1.6  发布 .aar
* 5.2  深入资源编译与打包
    * 5.2.1  AAPT2 与增量编译
    * 5.2.2  中间产物：.flat 文件格式浅析
    * 5.2.3  最终产物的文件格式浅析
        * 5.2.3.1  resources.arsc 二进制资源索引表
        * 5.2.3.2  二进制 *.xml
    * 5.2.4  资源编译产物的修改点
* 5.3  架构应用：为启动图标加上蒙层 - ScratchPaper 插件
    * 5.3.1  需求/问题：测试设备需安装多个变种 APK
    * 5.3.2  Extension 接口设计
    * 5.3.3  任务核心逻辑之为 JPG、PNG、Vector 图片添加蒙层
    * 5.3.4  任务核心逻辑之利用 AAPT2 编译新图片
    * 5.3.5  利用 Polyfill 获取额外的工件与工具
    * 5.3.6  集成与使用
* 5.4  架构应用：自动化 BundleTool 转换流程 - BundleTool 插件
    * 5.4.1  需求/问题：手动执行、测试、发布的繁琐
    * 5.4.2  Extension 接口设计
    * 5.4.3  任务设计思路解析——从 AGP 获取灵感
    * 5.4.4  利用 Polyfill 获取额外的工件与工具
    * 5.4.5  提供对外扩展点
    * 5.4.6  集成与使用


## 第六章  代码构建扩展之 Kotlin 源码

* 6.1  AGP 源码交互 API 进阶使用
    *  6.1.1  SourceSet 文件共享
    *  6.1.2  添加自定义的 Source Type
    *  6.1.3  添加动态生成的 Kotlin/Java 源码
* 6.2  Kotlin Symbol Processing (KSP)
    *  6.2.1  功能、架构一览
    *  6.2.2  注解处理器对比：KAPT，KSP
    *  6.2.3  KSP 处理器入口：SymbolProcessorProvider 与 SymbolProcessor
    *  6.2.4  KSP 主流程：process(Resolver) 与 finish() 方法
    *  6.2.5  访问者模式：遍历和获取符号/元素信息
    *  6.2.6  不止于注解：自定义过滤器
    *  6.2.7  文件依赖与增量处理
    *  6.2.8  复杂传参：KSP 处理器与 Gradle API 结合
    *  6.2.9  KSP 处理器测试
* 6.3  架构应用：源码阶段的路由表生成 - Koncat 插件
    *  6.3.1  需求/问题：注解处理器无法跨模块处理
    *  6.3.2  四类路由表生成方案
    *  6.3.3  Koncat 的生成方案：在源码阶段与依赖交互
    *  6.3.4  Koncat 的扩展方案：与外部注解处理器交互
    *  6.3.5  Koncat 的接口优化：为路由增加编译期 Stub
    *  6.3.6  Koncat 的项目结构：插件 + 多个 Symbols 处理器


## 第七章  代码构建扩展之 JVM 字节码

* 7.1  字节码简介
    * 7.1.1  JVM 字节码和 Dalvik 字节码
    * 7.1.2  查看源码对应的 JVM 字节码
    * 7.1.3  JVM 字节码修改常用工具与场景
    * 7.1.4  实战演示：替换 WakeLock 实现 & 验证修改结果
* 7.2  AGP 7.0 之前的字节码修改 API：Transform API
    * 7.2.1  AGP Transform API 简介
    * 7.2.2  实战演示：AGP Transform API 与 WakeLock 监控
    * 7.2.3  增量编译与并行执行
    * 7.2.4  Transform API 的优缺点
* 7.3  AGP 7.0（含）之后的字节码修改 API：Artifacts API
    * 7.3.1  Artifacts API 用法与版本更迭
    * 7.3.2  实战演示：Artifacts API 与 WakeLock 监控
    * 7.3.3  并行执行
    * 7.3.4  多个 Transform 任务
    * 7.3.5  溯源 `forScope`
* 7.4  AGP 7.0（含）之后的字节码修改 API：Instrumentation API
    * 7.4.1  Instrumentation API 用法与版本更迭
    * 7.4.2  实战演示：Instrumentation API 与 WakeLock 监控
    * 7.4.3  溯源 Instrumentation API
* 7.5  架构应用：敏感 API 调用的监控与代理 - Caliper 插件
    * 7.5.1  需求/问题：无法把控全局的权限申请和隐私数据访问
    * 7.5.2  注解和代理类的定义
    * 7.5.3  AGP 字节码处理 API 的选择：Instrumentation 还是 Variant API？
    * 7.5.4  突破 Instrumentation API 的局限：支持小范围预收集场景
    * 7.5.5  字节码处理的核心实现
    * 7.5.6  集成与使用
    * 7.5.7  总结


## 第八章  提升构建体验

* 8.1  构建分析
    * 8.1.1  Build Scan
    * 8.1.2  Gradle Enterprise 商业版
    * 8.1.3  Build Profile
    * 8.1.4  Build Analyzer
* 8.2  构建提速技巧
    * 8.2.1  减负
    * 8.2.2  缓存与增量
    * 8.2.3  并发
    * 8.2.4  其他优化小技巧
* 8.3  根据 Variant 决定是否启用插件
    * 8.3.1  从用户角度、插件开发角度分别思考
    * 8.3.2  在 productFlavors{} 配置中引用插件
    * 8.3.3  基于键入的命令引用插件
    * 8.3.4  禁用插件 Task
    * 8.3.5  在 Task 注册时拦截


## 附录

* 附1：
* 附2：
