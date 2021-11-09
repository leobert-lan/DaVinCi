## 移除废弃内容

注意，这些是彻底移除了！

```kotlin
//@Deprecated("", ReplaceWith("this.daVinCiShape(str)"))
//fun View.daVinCi(str: String) {
//    this.daVinCiShape(str)
//}

//@BindingAdapter("daVinCiBgStyle")
//@Deprecated("含义不恰当", ReplaceWith("this.daVinCiStyle(styleName)"))
//fun View.daVinCiBgStyle(styleName: String) {
//    this.daVinCiStyle(styleName)
//}

//class DaVinCi {
//    constructor(text: String?, view: View)
//}

//class DaVinCiExpression {
//    ColorStateList {
//        @Deprecated("表意不准确", ReplaceWith("color(color).states(state...)"))
//        fun apply(state: State, color: String): ColorStateList {
//            return color(color).states(state)
//        }
//
//        @Deprecated("表意不准确", ReplaceWith("color(color).states(state...)"))
//        fun apply(state: String, color: String): ColorStateList {
//            return color(color).states(state)
//        }
//
//        @Deprecated("表意不准确", ReplaceWith("color(color).states(state...)"))
//        fun apply(state: State, @ColorInt color: Int): ColorStateList {
//            return color(color).states(state)
//        }
//    }
//}
```

移除id： `<item name="log_tag" type="id"/>`

## 新增API

### DataBinding API
```kotlin
@BindingAdapter("daVinCi_bg1", "states1", requireAll = false)
fun View.daVinCiBg1(exp: DaVinCiExpression.Shape, states: Array<State>? = null) {
    this.helpXmlDaVinCiBg(exp, states)
}
```

该系列API从1到8 一共有8个， 用于替换废弃的：

```kotlin
@BindingAdapter(
    "daVinCi_bg", "daVinCi_bg_pressed", "daVinCi_bg_unpressed",
    "daVinCi_bg_checkable", "daVinCi_bg_uncheckable", "daVinCi_bg_checked", "daVinCi_bg_unchecked",
    requireAll = false
)
fun View.daVinCi(
    normal: DaVinCiExpression? = null,
    pressed: DaVinCiExpression? = null, unpressed: DaVinCiExpression? = null,
    checkable: DaVinCiExpression? = null, uncheckable: DaVinCiExpression? = null,
    checked: DaVinCiExpression? = null, unchecked: DaVinCiExpression? = null,
) {
    //...
}
```

使用参考：

```xml

<xxx>

    <CheckBox daVinCi_bg1="@{DaVinCiExpression.shape().corner(60).solid(`@i1`).stroke(`4dp`,`@i2`)}"
        daVinCi_bg2="@{DaVinCiExpression.shape().corner(60).solid(`@i2`).stroke(`4dp`,`@i1`)}"
        states1="@{State.array(State.CHECKED_T)}"
        states2="@{State.array(State.CHECKED_F,State.ENABLE_T)}"
        android:text="new DataBinding Feature">

        <tag android:id="@id/i1" android:value="@color/colorPrimaryDark" />

        <tag android:id="@id/i2" android:value="@color/colorAccent" />

    </CheckBox>

    <CheckBox daVinCi_bg1="@{DaVinCiExpression.shape().states(State.CHECKED_T).corner(60).solid(`@i1`).stroke(`4dp`,`@i2`)}"
        daVinCi_bg2="@{DaVinCiExpression.shape().states(State.CHECKED_F,State.ENABLE_T).corner(60).solid(`@i2`).stroke(`4dp`,`@i1`)}"
        android:text="new DataBinding Feature">

        <tag android:id="@id/i1" android:value="@color/colorPrimaryDark" />

        <tag android:id="@id/i2" android:value="@color/colorAccent" />

    </CheckBox>
</xxx>

```

最终使用的是StateListDrawable，可以直接指定shape对应的state，或者拆分到同号的statesX中声明。

### 直接使用Expression的简化API

```kotlin
//设置给View作为背景
osp.leobert.android.davinci.DaVinCiExpression.StateListDrawable.applyInto(view:View)

//设置给View作为背景
osp.leobert.android.davinci.DaVinCiExpression.Shape.applyInto(view:View)

//设置给TextView作为文字颜色
osp.leobert.android.davinci.DaVinCiExpression.ColorStateList.applyInto(textview:TextView)
```


## 啰嗦几句

先前我过于执着保留"从文本语法式解析出内容"的功能，实质上在Android内使用时，我们只会从对象操作入手。

而这一执着确实额外付出了一些性能代价！-- 在非必要时反向还原了语法式 以及 非必要情况下借用语法式解析逻辑实现了赋值逻辑

后续版本中我将探索一些方式减少性能开销
