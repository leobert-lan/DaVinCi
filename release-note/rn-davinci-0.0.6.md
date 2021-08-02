# Release Note: DaVinCi-0.0.6

这是一次很大的变更，DaVinCi从编写之初，其目标就是 **方便**，但单纯的方便是不够的，还要足够准确。

所以此次升级，将绝大多数 `不够准确` 的内容进行了纠正。所以变更非常的大

## 变更概述

### DaVinCiCore

变更的核心，在早先的版本中，并没有真正 `正确的` 支持 ColorStateList 和 StateListDrawable 的构建，本身它仅用于构建GradientDrawable，后续做了粗暴的扩展。

此次在设计上做出了重大变更：定义任意一个item时，可指定正确的组合状态； 而早先的版本中，定义任意一个item时，指定的是一个单一的状态，例如 `enabled=true`, 但实际情况并没有这么简单

而考虑到 `DaVinCiCore` 本身作为内部支持类，我直接移除了错误的部分，所以直接使用了 `DaVinCiCore` 开发的，**无法直接迁移**，需要修改自行开发的功能。
 
### Pool化复用
为了更快的进行构建，减少创建对象的损耗，这一期开始采用Pool设计，而用户可能接触到的是 `DaVinCi` 类：

* 需要修改实例获取方式（建议都改掉）
* 增加释放时重置到Pool的API调用 （能加的地方最后都加上，否则Pool没有实质意义）

后续优化版本中，会对语法树部分进一步采用Pool设计，但这属于内部设计变更，

### 配合 DaVinCiCore 变更而带来的变更
在样式定义、StateListDrawable、ColorStateList 方面，修改了相关的API，具体内容见下文

## 废弃

### 样式定义-SLD

```
osp.leobert.android.davinci.StyleRegistry.Style.register(state: State, expression: DaVinCiExpression)
```

显然，这样无法使用组合状态。

考虑到简便性，我们直接使用新功能 `DaVinCiExpression.StateListDrawable` ：

```
osp.leobert.android.davinci.StyleRegistry.Style.registerSld(exp: DaVinCiExpression.StateListDrawable)
```


例如：

```kotlin
class DemoStyle2 : StyleRegistry.Style("btn_style.test") {
    init {
        this.register(
            state = State.ENABLE_F,
            expression = DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")
        ).register(
            state = State.ENABLE_T,
            expression = DaVinCiExpression.shape().rectAngle().corner("10dp")
                .gradient("#ff3c08", "#ff653c", 0)
        )
    }
}
```

参考DemoStyle1 进行修改

``` kotlin
class DemoStyle : StyleRegistry.Style("btn_style.main") {
    init {
        this.registerSld(
            exp = DaVinCiExpression.stateListDrawable()
                .shape(DaVinCiExpression.shape().stroke("1", "#ff653c").corner("2dp"))
                .states(State.CHECKED_F, State.ENABLE_T)

                .shape(DaVinCiExpression.shape().solid("#ff653c").corner("2dp,2dp,0,0"))
                .states(State.CHECKED_T, State.ENABLE_T)

                .shape(DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")).states(State.ENABLE_F)

        )
    }
}
```

### ColorStateList 表意性修正

```
osp.leobert.android.davinci.DaVinCiExpression.ColorStateList.apply(osp.leobert.android.davinci.State, java.lang.String)
osp.leobert.android.davinci.DaVinCiExpression.ColorStateList.apply(java.lang.String, java.lang.String)
osp.leobert.android.davinci.DaVinCiExpression.ColorStateList.apply(osp.leobert.android.davinci.State, int)
```

三组API同样因无法满足组合状态而被废弃。

使用

```
osp.leobert.android.davinci.DaVinCiExpression.ColorStateList.color(java.lang.String)
osp.leobert.android.davinci.DaVinCiExpression.ColorStateList.color(int)
```
后，继续调用
 `osp.leobert.android.davinci.Statable.states(osp.leobert.android.davinci.State...)` 
或
 `osp.leobert.android.davinci.Statable.states(java.lang.String...)` 设置 `单一状态` 或 `组合状态`

### DataBinding的Adapter

`View.daVinCiBgStyle(styleName: String)`, `@BindingAdapter("daVinCiBgStyle")` 含义不准确，进行了调整

```kotlin
@BindingAdapter("daVinCiBgStyle")
@Deprecated("含义不恰当", ReplaceWith("this.daVinCiStyle(styleName)"))
fun View.daVinCiBgStyle(styleName: String) {
    this.daVinCiStyle(styleName)
}

@BindingAdapter("daVinCiStyle")
fun View.daVinCiStyle(styleName: String) {
   //...
}
```

### State枚举太啰嗦

```kotlin
    @Deprecated("too long",replaceWith = ReplaceWith("CHECKABLE_T"))
    STATE_CHECKABLE_TRUE(android.R.attr.state_checkable),
    @Deprecated("too long",replaceWith = ReplaceWith("CHECKABLE_F"))
    STATE_CHECKABLE_FALSE(-android.R.attr.state_checkable),

    @Deprecated("too long",replaceWith = ReplaceWith("CHECKED_T"))
    STATE_CHECKED_TRUE(android.R.attr.state_checked),
    @Deprecated("too long",replaceWith = ReplaceWith("CHECKED_F"))
    STATE_CHECKED_FALSE(-android.R.attr.state_checked),

    @Deprecated("too long",replaceWith = ReplaceWith("ENABLE_T"))
    STATE_ENABLE_TRUE(android.R.attr.state_enabled),
    @Deprecated("too long",replaceWith = ReplaceWith("ENABLE_F"))
    STATE_ENABLE_FALSE(-android.R.attr.state_enabled),

    @Deprecated("too long",replaceWith = ReplaceWith("SELECTED_T"))
    STATE_SELECTED_TRUE(android.R.attr.state_selected),
    @Deprecated("too long",replaceWith = ReplaceWith("SELECTED_F"))
    STATE_SELECTED_FALSE(-android.R.attr.state_selected),

    @Deprecated("too long",replaceWith = ReplaceWith("PRESSED_T"))
    STATE_PRESSED_TRUE(android.R.attr.state_pressed),
    @Deprecated("too long",replaceWith = ReplaceWith("PRESSED_F"))
    STATE_PRESSED_FALSE(-android.R.attr.state_pressed),

    @Deprecated("too long",replaceWith = ReplaceWith("FOCUSED_T"))
    STATE_FOCUSED_TRUE(android.R.attr.state_focused),
    @Deprecated("too long",replaceWith = ReplaceWith("FOCUSED_F"))
    STATE_FOCUSED_FALSE(-android.R.attr.state_focused),
```

下个版本将不再使用枚举，但依旧可支持字符串的映射

### API命名不恰当修改 

```
@Deprecated("", ReplaceWith("this.daVinCiShape(str)"))
fun View.daVinCi(str: String) {
    this.daVinCiShape(str)
}
```

## 新增

### StateListDrawable

```
osp.leobert.android.davinci.DaVinCiExpression.Companion.stateListDrawable
```

e.g.

```
DaVinCiExpression.stateListDrawable()
.shape(DaVinCiExpression.shape().stroke("1", "#ff653c").corner("2dp"))
.states(State.CHECKED_F, State.ENABLE_T)

.shape(DaVinCiExpression.shape().solid("#ff653c").corner("2dp,2dp,0,0"))
.states(State.CHECKED_T, State.ENABLE_T)

.shape(DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")).states(State.ENABLE_F)
```

构建一个描述StateListDrawable的语法树，并进行使用

同样，在内部支持了字符串的语法树解析：

```
binding.test2.setOnClickListener {
            it.daVinCiSld("""
                sld:[ 
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
            """.trimIndent())
        }
```


### DaVinCi 实例获取与回收

废弃了原先的构造函数，推荐使用：

```
osp.leobert.android.davinci.DaVinCi.Companion.of(text: String? = null, applier: Applier? = null): DaVinCi
```

获取实例

当使用后，进行释放调用

```
osp.leobert.android.davinci.DaVinCi.release()
```

### 消费者Applier

在先前的版本中，我们简单的认为最终目标就是设置View的背景或者TextView的TextColor，但是这有点局限了。

在获取DaVinCi实例时，我们提供自定义的：

```
osp.leobert.android.davinci.Applier
```
实例，即可自行定义如何使用

当然，为了最大程度保留简单的特点，同样提供了扩展函数：

```
//消费类型为设置View的背景
fun View.viewBackground() = ViewComposer(this).apply {
    this.drawableConsumer = { ViewCompat.setBackground(this@apply.view, this) }
}

//消费类型为设置View的背景
fun <T : View> ViewComposer<T>.viewBackground(): ViewComposer<T> {
    this.drawableConsumer = { ViewCompat.setBackground(this@viewBackground.view, this) }
    return this
}

//消费类型为设置TextView的textColor
fun TextView.csl() = ViewComposer(this).apply {
    this.cslConsumer = { if (this != null) this@apply.view.setTextColor(this) }
}

//如果是TextView，既设置背景也设置textColor，否则设置背景
fun View.applier(): Applier {
    return when (this) {
        is TextView -> this.csl().viewBackground()
        else -> this.viewBackground()
    }
}
```