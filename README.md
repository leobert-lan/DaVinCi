## 是什么？
在Android上取代xml方式定义 Shape/GradientDrawable 以及 ColorStateList的方案。

* 支持在 Java/Kotlin 业务代码 中使用
* 配合 DataBinding 可以在 XML布局文件 中使用

## 哪些情况下需要它

> * 觉得xml太啰嗦
> * 命名困难，资源管理困难
> * 项目样式非常多但复用度不高
> * UI做不到对样式规范化管理
> * 切换一个文件打断思路的成本太高


## 为什么叫 DaVinCi
最开始用于解决 Shape/GradientDrawable，而几何类绘制是毕加索最擅长的，然而这个名字早已被使用，梵高又太抽象了，索性就叫达芬奇了，
毕竟我也很讨厌命名。

Picasso has been used, Van Gogh's paints is too abstract, thus da VinCi ran into my mind.

## 如何使用

目前已迁移发布到MavenCentral

```
allprojects {
    repositories {
        mavenCentral()
    }
}
```

and dependency in module's build.gradle:

```
implementation "io.github.leobert-lan:davinci-anno:0.0.2" //注解
kapt or ksp "io.github.leobert-lan:davinci-anno-ksp:0.0.2" //注解处理器，支持kapt或者ksp
implementation "io.github.leobert-lan:davinci:0.0.5" //核心库
debugImplementation "io.github.leobert-lan:davinci-style-viewer:0.0.1" //预览

```

## 具体使用方式

详见：[leobert.github.io/DaVinCi](https://leobert-lan.github.io/repo/DaVinCi.html)

或参考项目Demo


## 几篇相关拙作：

* 实现原理解析，详见拙作：[好玩系列：拥有它，XML文件少一半--更方便的处理View背景](https://leobert-lan.github.io/Android/Drawable/post_4.html)
* 关于注解处理器（ksp实现）以及实现的核心目标，详见拙作： [好玩系列 | 拥抱Kotlin Symbol Processing(KSP),项目实战](https://leobert-lan.github.io/Android/KSP/post_24.html)

