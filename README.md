[![GitHub](https://img.shields.io/github/license/leobert-lan/DaVinCi)](https://github.com/leobert-lan/DaVinCi/blob/master/LICENSE)

## 如果报错

如果出现guava中类重复，可添加进行解决

```
implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")
```

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

最开始用于解决 Shape/GradientDrawable，而几何类绘制是毕加索最擅长的，然而这个名字早已被使用，梵高又太抽象了，索性就叫达芬奇了， 毕竟我也很讨厌命名。

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

最新版本：

* <img src="https://img.shields.io/static/v1?label=MavenCentray&message=davinci"/> : [<img src="https://img.shields.io/maven-central/v/io.github.leobert-lan/davinci.svg?label=latest%20release"/>](https://search.maven.org/search?q=g:io.github.leobert-lan%20And%20a:davinci)

* <img src="https://img.shields.io/static/v1?label=MavenCentray&message=davinci-anno"/> : [<img src="https://img.shields.io/maven-central/v/io.github.leobert-lan/davinci-anno.svg?label=latest%20release"/>](https://search.maven.org/search?q=g:io.github.leobert-lan%20And%20a:davinci-anno)

* <img src="https://img.shields.io/static/v1?label=MavenCentray&message=davinci-anno-ksp"/> : [<img src="https://img.shields.io/maven-central/v/io.github.leobert-lan/davinci-anno-ksp.svg?label=latest%20release"/>](https://search.maven.org/search?q=g:io.github.leobert-lan%20And%20a:davinci-anno-ksp)

* <img src="https://img.shields.io/static/v1?label=MavenCentray&message=davinci-style-viewer"/> : [<img src="https://img.shields.io/maven-central/v/io.github.leobert-lan/davinci-style-viewer.svg?label=latest%20release"/>](https://search.maven.org/search?q=g:io.github.leobert-lan%20And%20a:davinci-style-viewer)

## 具体使用方式

详见：[leobert.github.io/DaVinCi](https://leobert-lan.github.io/repo/DaVinCi.html)

或参考项目Demo

### API 功能简介

DaVinCi 目前提供三类功能：

* func1:通过 `逻辑构建` 、`反序列DSL` 定制Drawable or ColorStateList 并进行设置
* func2:配置 Drawable or ColorStateList 的 Style 或 StyleFactory 并进行应用
* func3:对 Style 或 StyleFactory 对应的 Drawable or ColorStateList 进行APP方式预览

其中，func1是主要功能。而func1有一个使用缺点："未复用或者不便于复用"，所以增加了func2提供了可复用性，并同步增加func3，增加了预览

#### func1系列 -- 构建AST

目前（0.0.8）版本实现的支持创建：

* ColorStateList
* GradientDrawable
* StateListDrawable

*为了方便描述，我们将这三类简称为Target*

而创建的方法有二：

* 直接基于实际需要的语法树结构，利用面向对象的封装构建出AST结构，传入上下文（DaVinCi）后，直接完成 Target 的构建。
* 传入语法树DSL（序列化的String），指定AST根节点，传入上下文（DaVinCi）后，解析出AST结构（区别于前者，这里是解析DSL建立AST），进而完成 Target 的构建

简而言之，这两者方式都是指导DaVinCi **创建怎样的Target**

而这个指导方式又存在两种场景：

* 在xml布局文件中，需要配合DataBinding，实质上，通过DataBinding和预置的BindingAdapter配置，最终运行时的细节等同场景2
* 在逻辑代码（Java、Kotlin）中直接使用

*我为什么实现这样的方案：在最初，我的目标就不仅止于更方便地 **定义和设置背景** ，而是希望透过序列化的信息传输+反序列化DSL以实现 **皮肤包、线上更新背景** 等功能，虽然它们距离实现还有点远*

##### ColorStateList

相对比较简单

```kotlin
//选中不是指获取焦点，而是CheckBox等选中的口语化表达
DaVinCiExpression.stateColor()
    .color(Color.parseColor("#ff00ff")).states(State.PRESSED_F, State.CHECKED_T) //选中 未按下
    .color(Color.parseColor("#0000dd")).states(State.PRESSED_T, State.CHECKED_T) //选中 按下
    .color(Color.parseColor("#ff0000")).states(State.CHECKED_T.name) //选中 实际上是冗余配置，会按照第一行的来
    .color(Color.parseColor("#00aa00")).states(State.CHECKED_F)// 未选中
```

##### GradientDrawable

对应xml定义Drawable资源的root-tag "shape"

**构建一个Shape的AST Root**

```kotlin
DaVinCiExpression.shape(): Shape
```

**指定Shape的类型**

一般常用的是rectAngle 和 oval

```kotlin
fun rectAngle(): Shape
fun oval(): Shape
fun ring(): Shape
fun line(): Shape
```

**rectAngle时的圆角**

```kotlin
fun corner(@Px r: Int): Shape
fun corner(str: String): Shape
fun corners(@Px lt: Int, @Px rt: Int, @Px rb: Int, @Px lb: Int): Shape
```

尺寸均可以 "xdp" 表达dp值，如"3dp"即为 3个dp，"3"则为3个px。

对于oval，可以使用corner标识其radius

目前（0.0.8）并未完整的支持ring

**填充色**

```kotlin
fun solid(str: String): Shape //色值 "#ffffffff" 或者 "rc/颜色资源名"
fun solid(@ColorInt color: Int): Shape
```

**描边**

```kotlin
fun stroke(width: String, color: String): Shape
fun stroke(width: String, color: String, dashGap: String, dashWidth: String): Shape
fun stroke(@Px width: Int, @ColorInt colorInt: Int): Shape 
```

**渐变**

```kotlin
fun gradient(
    type: String = Gradient.TYPE_LINEAR,
    @ColorInt startColor: Int,
    @ColorInt endColor: Int,
    angle: Int = 0
): Shape

fun gradient(
    type: String = Gradient.TYPE_LINEAR, @ColorInt startColor: Int,
    @ColorInt centerColor: Int?, @ColorInt endColor: Int,
    centerX: Float,
    centerY: Float,
    angle: Int = 0
): Shape

fun gradient(startColor: String, endColor: String, angle: Int): Shape

fun gradient(type: String = Gradient.TYPE_LINEAR, startColor: String, endColor: String, angle: Int = 0): Shape

fun gradient(
    type: String = Gradient.TYPE_LINEAR,
    startColor: String,
    centerColor: String?,
    endColor: String,
    centerX: Float,
    centerY: Float,
    angle: Int
): Shape

```

*尺寸说明：0.0.8之前，只能使用px或dp，e.g.: '1' is one pixel,'1dp' is one dp; 0.0.8及其以后，增加了 '1pt','1mm','1sp'等*

##### StateListDrawable

以此为例：

```kotlin
DaVinCiExpression.stateListDrawable()
    .shape(DaVinCiExpression.shape().stroke("1", "#ff653c").corner("2dp"))
    .states(State.CHECKED_F, State.ENABLE_T)

    .shape(DaVinCiExpression.shape().solid("#ff653c").corner("2dp,2dp,0,0"))
    .states(State.CHECKED_T, State.ENABLE_T)

    .shape(DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")).states(State.ENABLE_F)
```

小结：以上以0.0.8版本为例，列举了 `逻辑构建` 的主要API，而 DSL 的规则将单独介绍（看到这些字表示我还在犯懒，没有写完）

#### func1补充 -- 使用AST生成的结果（Drawable、ColorStateList）

构建完AST后就具备了创建Target的能力，但我们最终的目标是使用Target，DaVinCi中对使用场景也进行了针对性封装：

* 给View设置背景 -- 内置策略
* 给TextView设置文字色 -- 内置策略
* 自由扩展

**一般情况下，运用：**：

```kotlin
DaVinCiExpression#applyInto(view: View)
```

即可为View按照 `内置策略` 使用 DaVinCiExpression 创建的Target。

DaVinCi 作为上下文，接受一个 Applier实例作为 `Target的消费者`，

```kotlin
interface Applier {
    val context: Context

    fun getTag(id: Int): Any?

    fun applyDrawable(drawable: Drawable?)

    fun applyColorStateList(csl: ColorStateList?)
}
```

并可以使用一下API运用内置策略：

```kotlin
//给View设置背景
fun View.viewBackground(): Applier

//给TextView设置文字色
fun TextView.csl(): Applier

//自动应用以上两者规则
fun View.applier(): Applier {
    return when (this) {
        is TextView -> this.csl().viewBackground()
        else -> this.viewBackground()
    }
}
```

通过如下API获得 `DaVinCi` 实例并设置 `Applier` 策略：

```kotlin
DaVinCi.of(text: String? = null, applier: Applier? = null): DaVinCi
```

_如果使用序列化的DSL，将其作为text 入参，并按照根类型调用下文API；否则text参数为null，基于手动创建的AST树调用以下API_

按照类型使用以下API：

```kotlin
class DaCinCi {

    fun applySld(exp: DaVinCiExpression.StateListDrawable)

    fun applySld(exp: DaVinCiExpression.StateListDrawable, onComplete: (() -> Unit)? = null)

    fun applyShape(exp: DaVinCiExpression.Shape)

    fun applyShape(exp: DaVinCiExpression.Shape, onComplete: (() -> Unit)? = null)

    fun applyCsl(exp: DaVinCiExpression.ColorStateList)

    fun applyCsl(exp: DaVinCiExpression.ColorStateList, onComplete: (() -> Unit)? = null)
}
```

可参考下文代码：

```kotlin

fun demoOfApplier() {
    val view = findViewById<TextView>(R.id.test2)

    val dsl = """ sld:[ 
            shape:[ 
                gradient:[ type:linear;startColor:#ff3c08;endColor:#353538 ];
                st:[ Oval ];
                state:[ ${State.ENABLE_T.name}|${State.PRESSED_T.name} ];
                corners:[ 40dp ];
                stroke:[ width:4dp;color:#000000 ]
            ];
            shape:[ 
                gradient:[ type:linear;startColor:#ff3c08;endColor:#353538 ];
                st:[ Oval ];
                corners:[ 40dp ];
                state:[ ${State.ENABLE_T.name} ];
                stroke:[ width:4dp;color:rc/colorAccent ]
            ];
            shape:[ 
                gradient:[ type:linear;startColor:#ff3c08;endColor:#353538 ];
                st:[ Oval ];
                state:[ ${State.ENABLE_F.name} ];
                corners:[ 40dp ];
                stroke:[ width:4dp;color:#000000 ]
            ]
        ]
        """.trimIndent()

    //内置策略
    DaVinCi.of(dsl, view.viewBackground())
        .applySld(DaVinCiExpression.StateListDrawable.of(false))

    DaVinCi.of(null, view.csl())
        .applyCsl(
            DaVinCiExpression.stateColor()
                .color("#e5332c").states(State.PRESSED_T)
                .color("#667700").states(State.PRESSED_F)
        )

    DaVinCi.of(null, view.applier())
        .applyCsl(
            DaVinCiExpression.stateColor()
                .color("#e5332c").states(State.PRESSED_T)
                .color("#667700").states(State.PRESSED_F)
        )

    //自由使用csl
    val daVinCi = DaVinCi.of(null, Applier.csl(this) {
        //自由使用
    })
    val cslExp = DaVinCiExpression.stateColor()
        .color("#e5332c").states(State.PRESSED_T)
        .color("#667700").states(State.PRESSED_F)

    daVinCi.applyCsl(cslExp)
    daVinCi.applyCsl(cslExp, onComplete = {
        //onComplete
    })


    //自由使用shape、sld
    val daVinCi2 = DaVinCi.of(null, Applier.drawable(this) {
        //自由使用
    })
    //shape 或者 stateListDrawable
    val drawableExp = DaVinCiExpression.shape().oval()
        .corner("40dp") //这个就没啥用了
        .solid(resources.getColor(R.color.colorPrimaryDark))
        .stroke(12, Color.parseColor("#26262a"))

    //如果为 stateListDrawable 则使用 applySld
    daVinCi2.applyShape(drawableExp)
    daVinCi.applyShape(drawableExp, onComplete = {
        //onComplete
    })

    //以上两者虽然相对简单，但存在一处缺陷：无法使用tag语法，没有实现从View中查询tag，如果需要使用View中的tag，则可以如下：

    val daVinCi3 = DaVinCi.of(null, Applier.ViewComposer(view)
        .drawable {
            //自由使用drawable
        }.csl {
            //自由使用ColorStateList
        }
    )

}
```

**在XML中使用：**

请结合Demo查阅，API含义很简单，不再赘述。

#### func2 配置 Drawable or ColorStateList 的 Style 或 StyleFactory 并进行应用

使用Style 或 StyleFactory需要使用注解，**按照项目实际使用AnnotationProcessor或Kapt（or ksp）**

以KAPT为例：

```
kapt {
    this.arguments {
        this.arg("daVinCi.verbose", "true") //编译日志开关
        this.arg("daVinCi.pkg", "com.example.simpletest") //生成类的package
        this.arg("daVinCi.module", "App") //生成类的前缀
        this.arg("daVinCi.preview", "false") //需要预览则打开
    }
}
```

引入注解和注解处理器
```
"io.github.leobert-lan:davinci-anno:0.0.2"
"io.github.leobert-lan:davinci-anno-ksp:0.0.2"
```

Application初始化时调用：`{daVinCi.module}DaVinCiStyles.register()` _{daVinCi.module} 为配置的前缀_

给定唯一的Style命名，并定义Style、StyleFactory：

```kotlin
//定义style
@DaVinCiStyle(styleName = "btn_style.main") //给定唯一的Style命名
@StyleViewer(
    height = 40, width = ViewGroup.LayoutParams.MATCH_PARENT,
    type = StyleViewer.FLAG_CSL or StyleViewer.FLAG_BG, background = "#ffffff"
)
class DemoStyle : StyleRegistry.Style("btn_style.main" /*给定唯一的Style命名*/) {
    init {
        Utils.timeCost("create DemoStyle 'btn_style.main'") {
            this.registerSld(
                exp = DaVinCiExpression.stateListDrawable()
                    .shape(DaVinCiExpression.shape().stroke("1", "#ff653c").corner("2dp"))
                    .states(State.CHECKED_F, State.ENABLE_T)

                    .shape(DaVinCiExpression.shape().solid("#ff653c").corner("2dp,2dp,0,0"))
                    .states(State.CHECKED_T, State.ENABLE_T)

                    .shape(DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")).states(State.ENABLE_F)

            ).registerCsl(
                exp = DaVinCiExpression.stateColor()
                    .color("#000000").states(State.ENABLE_T, State.CHECKED_T)
                    .color("#666666").states(State.ENABLE_T, State.CHECKED_F)
                    .color("#ffffff").states(State.ENABLE_F)
            )
        }
    }
}

//定义StyleFactory
@DaVinCiStyleFactory(styleName = "btn_style.sub") //给定唯一的Style命名
class DemoStyleFactory : StyleRegistry.Style.Factory() {
  override val styleName: String = "btn_style.sub" //给定唯一的Style命名

  override fun apply(style: StyleRegistry.Style) {
    style.registerSld(
      DaVinCiExpression.stateListDrawable()
        .shape(DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp"))
        .states(State.ENABLE_F)
        .shape(
          DaVinCiExpression.shape().rectAngle().corner("10dp")
            .gradient("#ff3c08", "#ff653c", 0)
        ).states(State.ENABLE_T)
    )
  }
}



```

并通过以下API使用：

```kotlin
@BindingAdapter("daVinCiStyle")
fun View.daVinCiStyle(styleName: String) {
    with(StyleRegistry.find(styleName)) {

        this?.applyTo(daVinCi = DaVinCi.of(null, this@daVinCiStyle.applier()), releaseAfter = true)
            ?: Log.d(DaVinCiExpression.sLogTag, "could not found style with name $styleName")
    }
}
```

#### func3使用APP形式预览Style、StyleFactory

_目前尚未开发DataBinding xml预览_

对于有复用度需求的Style、StyleFactory 可以进行预览：

添加：io.github.leobert-lan:davinci-style-viewer:0.0.1 并在Application中调用： ` AppDaVinCiStylePreviewInjector.register()` _注意配置的前缀_

桌面会多出一个Activity入口。进入后是一个列表，列表的ItemView应用了样式，并可以设置Enable、Checked

当然，需要对Style和StyleFactory使用如下注解：

```kotlin
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
public annotation class StyleViewer(
    val height: Int = 48, // 预览区高度 dp
    val width: Int = -1 /*android.view.ViewGroup.LayoutParams.MATCH_PARENT = -1*/, //预览区宽度 dp
    val background: String = "#ffffff", 预览区背景，可避免撞色
    val type: Int = FLAG_BG or FLAG_CSL, //用途，使用默认即可
) {
    public companion object {
        public const val FLAG_BG: Int = 1 shl 0
        public const val FLAG_CSL: Int = 1 shl 1
    }
}
```

## 项目内容简介

* anno_ksp ：style注册和预览的 APT or KSP
* annotation ： style 注册与预览配置相关的注解
* davinci ：主功能API
* davinci_styles_viewer ：扩展，用于样式预览
* app: Demo
* 废弃
    * preview_anno_ksp：废弃，style注册和预览的KSP实现
    * annotation-java：废弃，实测KSP是否对Java注解有效

## 几篇相关拙作：

* 实现原理解析，详见拙作：[好玩系列：拥有它，XML文件少一半--更方便的处理View背景](https://leobert-lan.github.io/Android/Drawable/post_4.html)
* 关于注解处理器（ksp实现）以及实现的核心目标，详见拙作： [好玩系列 | 拥抱Kotlin Symbol Processing(KSP),项目实战](https://leobert-lan.github.io/Android/KSP/post_24.html)

## 重大变更版本的ReleaseNote

* [DaVinCi-0.0.6](./release-note/rn-davinci-0.0.6.md) -- 让功能更加准确
* [DaVinCi-0.0.7](./release-note/rn-davinci-0.0.7.md) -- 移除了了0.0.6中大部分标记废除的内容，提供了更多方便的API

## TODO

0.0.8 性能优化：对象池化，平滑内存使用，减少GC && 解析任务分离到工作线程，提高主线程利用率（大量的数组、集合、字符串操作确实耗时）
