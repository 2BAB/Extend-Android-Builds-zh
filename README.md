# 《Android 构建与架构实战》资料仓库

## 购买地址

[电子版](https://t.zsxq.com/0eF9jWLpY)：原价 ¥499，点击链接或扫描下面二维码购买，进入后请参考下图点击专栏阅读（推荐使用网页版获得最佳阅读体验）。本书目录见该[文档](./TOC.md)。

![封面与星球二维码](cover-and-planet-qrcode.png)

![专栏阅读体验](read-in-web-page.png)


星球主营内容：

- 《Android 构建与架构实战》电子专栏：可能是市面上独一份（包括中英文）、系统性学习 Gradle 和 AGP 实战的书籍。随着 Android 的发展，封禁的系统 API 越来越多，Runtime 的黑科技已经越来越没得做，编译构建的工具却热度不减。
- 新知预告，不仅是英文社区的一手 Android 架构方面资料，还包括作为 GDE 参与到各个会议、与行业专家交流等了解到的前沿资讯。
- 会员播客，包括软技能分享、行业观察、职场思考、海外的互联网公司发展等。
- 问答：技术问题包括 AGP、Gradle、Android 架构、Kotlin 编译器等我和嘉宾探索的领域，均可提供一定的指引和帮助；软实力和职业发展方向的问题亦可分享讨论。


## 前序学习资料

- [《KOGE》](https://koge.2bab.me/#/zh-cn/) 小册（已开源），前置学习资料：KOGE 是 Kotlin-oriented Gradle Essentials 的缩写，顾名思义是面向 Kotlin 的 Gradle 基础手册。我们按照合理的先后顺序，列出新手最困惑的概念，再从一些互联网上已有的问题、源码、示例项目中去学习。它不是 “Awesome Gradle” 的项目收藏夹，而是一份大纲，一本简练的自学手册。手册选择了 Kotlin 作为介绍 DSL 脚本和插件开发的语言，链接和用例以 Android 构建场景为主。Gradle Kotlin DSL 在 IDE 中的补全支持优秀，Kotlin 的生态活跃，作为 Android 开发者的我也十分享受一门语言带来的统一体验（App 主体开发和构建工具开发）。对于其他平台的 Gradle 使用者，也可以沿袭同样的脉络进行学习。


## 配套代码

- [Playground](./Playground/) ：本书配套示例代码。
- [Polyfill](https://github.com/2BAB/Polyfill)：本书使用到的开源项目，构建了一套与 AGP Artifact API 风格一致的第三方工件仓库。
- [BundleTool Gradle Plugin](https://github.com/2BAB/bundle-tool-gradle-plugin)：本书使用到的开源项目，自动化 BundleTool 执行的 Gradle 插件，包括 `.apks` 的生成、测试、安装、上传等后续操作。
- [ScratchPaper](https://github.com/2BAB/ScratchPaper)：本书使用到的开源项目，一款图片资源相关的 Gradle 插件，用来给 APK 图标添加 variant/version/git-commit-id 等信息以区分不同版本，由全新的 Variant/Artifact API 和 Polyfill 框架驱动。
- [Koncat](https://github.com/2BAB/Koncat)：本书使用到的开源项目，基于 KSP，不需要反射或字节码修改，即可在编译期的源码阶段实现多模块的标记收集，路由表生成等，例如获取一个应用里某个接口的所有实现。
- [Caliper](https://github.com/2BAB/Caliper)：本书使用到的开源项目，Android 敏感权限和 API 调用的监控和控制器，基于 AGP 新版字节码转换 API。
- [Detekt](https://github.com/detekt/detekt)：Kotlin 静态代码检查工具。
- [Kotlin-Code-Analyzer](https://github.com/bennyhuo/kotlin-code-analyzer)：Kotlin 原生静态检查 API 封装工具。


## 更多参考资料

- [附录 - 常用缩写/常用翻译](./Common-Abbreviation-Translation.md)
- [本书各节参考资料](./reference_per_section.md)
- [Gradle 插件收藏夹](https://github.com/stars/2BAB/lists/gradle-plugins)


## 导读




## 勘误

如果读者在文本和代码中发现错误，希望能帮忙提交建议与勘误。请直接访问本仓库的 issues 页卡，按照 issue template 的提示提交勘误（文本和代码的勘误均相同）。一方面能减少其他读者的困惑，另一方面也帮助我提高后续版本的输出质量。在此，由衷地感谢每一位购买和阅读本书的读者。

