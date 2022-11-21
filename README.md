# 《Android 构建与架构实战》资料仓库

## 购买地址

- [电子版](https://xiaozhuanlan.com/extend-android-builds-zh)：连载中，预计 2023 年 2月 28 日完结，原价 ¥399，完结前将保持优惠价（¥199 起跳，每两周涨价 ￥20）。

## 前序学习资料

- [《KOGE》](https://koge.2bab.me/#/zh-cn/) 小册（已开源），前置学习资料：KOGE 是 Kotlin-oriented Gradle Essentials 的缩写，顾名思义是面向 Kotlin 的 Gradle 基础手册。我们按照合理的先后顺序，列出新手最困惑的概念，再从一些互联网上已有的问题、源码、示例项目中去学习。它不是 “Awesome Gradle” 的项目收藏夹，而是一份大纲，一本简练的自学手册。手册选择了 Kotlin 作为介绍 DSL 脚本和插件开发的语言，链接和用例以 Android 构建场景为主。Gradle Kotlin DSL 在 IDE 中的补全支持优秀，Kotlin 的生态活跃，作为 Android 开发者的我也十分享受一门语言带来的统一体验（App 主体开发和构建工具开发）。对于其他平台的 Gradle 使用者，也可以沿袭同样的脉络进行学习。

## 配套代码

- [Playground](https://github.com/2BAB/Extend-Android-Builds-Playground-zh) ：本书配套示例代码仓库。
- [Polyfill](https://github.com/2BAB/Polyfill)：本书使用到的开源项目，构建了一套与 AGP Artifact API 风格一致的第三方工件仓库。
- [Seal](https://github.com/2BAB/Seal)：本书使用到的开源项目，一款处理 AndroidManifest.xml 合并冲突的 Gradle 插件，由全新的 Variant/Artifact API 和 Polyfill 框架驱动。
- [ScratchPaper](https://github.com/2BAB/ScratchPaper)：本书使用到的开源项目，一款图片资源相关的 Gradle 插件，用来给 APK 图标添加 variant/version/git-commit-id 等信息以区分不同版本，由全新的 Variant/Artifact API 和 Polyfill 框架驱动。
- [Koncat](https://github.com/2BAB/Koncat)：本书使用到的开源项目，基于 KSP，不需要反射或字节码修改，即可在编译期的源码阶段实现多模块的标记收集，路由表生成等，例如获取一个应用里某个接口的所有实现。

## 更多参考资料

- [本书各节参考资料](./reference_per_section.md)
- [Gradle 插件收藏夹](https://github.com/stars/2BAB/lists/gradle-plugins)

## 导读

- [我与 Gradle 的十年]()

## 勘误

如果读者在文本和代码中发现错误，希望能帮忙提交建议与勘误。一方面能减少其他读者的困惑，另一方面也帮助我们提高后续版本的输出质量。在此，由衷地感谢每一位购买和阅读本书的读者。

- 对于文本的勘误：请直接访问本仓库的 Issues 页卡，按照 issue template 提交勘误。
- 对于代码的勘误：若不确定问题出现的场景、复现步骤，请按如上文本勘误的步骤先提交问题；若已确定，请访问 [Extend-Android-Builds-samples-zh](https://github.com/2BAB/Extend-Android-Builds-samples-zh) 提交 issue / PR。

