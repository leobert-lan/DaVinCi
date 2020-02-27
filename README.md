# DaVinCi （达·芬奇）
An Android library to help create background drawable and ColorStateList without xml, writen in kotlin, support Java/Koltin code invoking or using in layout-xml with DataBinding

用于便捷的创建背景drawable以及ColorStateList，再也不用创建XML了！！！可以在Java或者kotlin代码中调用，也可以在布局文件中使用（需要使用DataBinding）

## Why named DaVinCi
Picasso has been used, Van Gogh's paints is too abstract, thus da VinCi ran into my mind.

## How to use

###  import library

Considering it may not be released to JCenter, declare the repo in root build.gradle

```
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url 'https://dl.bintray.com/leobert-lan-oss/maven/'
        }
    }
}
```

and dependency in module's build.gradle:

```
implementation 'osp.leobert.android:davinci:{version}'
```

current version is 0.0.1, an alpha version, much more testing and apis is needing

### use in Java/Kotlin code

```
binding.test.setOnClickListener {
    it.daVinCi(
        normal = DaVinCiExpression.shape().oval()
            .corner("40dp") //这个就没啥用了
            .solid(resources.getColor(R.color.colorPrimaryDark))
            .stroke(12,Color.parseColor("#26262a"))
    )
}
```

or another crazy way:

```
//这种不推荐使用啊，考虑语法解析一方面是考虑方便打印，另一方面是考虑到以后替换xml内的方案时，可以有后手
binding.test2.setOnClickListener {
    it.daVinCi("shape:[ gradient:[ type:linear;startColor:#ff3c08;endColor:#353538 ]; st:[ Oval ]; corners:[ 40dp ]; stroke:[ width:4dp;color:rc/colorAccent ] ]")
}
```


###  used in xml with DataBinding

```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="a"
            type="String" />

        <import type="osp.leobert.android.davinci.DaVinCiExpression" />

    </data>

    <androidx.core.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

        <LinearLayout
            daVinCi_bg="@{DaVinCiExpression.shape().solid(`#eaeaea`)}"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="10dp">

            <TextView
                android:id="@+id/test"
                daVinCi_bg="@{DaVinCiExpression.shape().corner(60).solid(`@i2`).stroke(`4dp`,`@i2`)}"
                android:layout_width="match_parent"
                android:layout_height="100dp"
                android:layout_marginTop="10dp"
                android:background="@drawable/test"
                android:gravity="center"
                android:text="@string/app_name">

                <tag
                    android:id="@id/i1"
                    android:value="@color/colorPrimaryDark" />

                <tag
                    android:id="@id/i2"
                    android:value="@color/colorAccent" />
            </TextView>

            <Button
                daVinCi_bg_pressed="@{DaVinCiExpression.shape().corner(`10dp,15dp,20dp,30dp`).stroke(`4dp`,`@i2`).gradient(`#26262a`,`#ff0699`,0)}"
                daVinCi_bg_unpressed="@{DaVinCiExpression.shape().corner(60).solid(`@i1`).stroke(`4dp`,`@i2`)}"
                android:layout_width="match_parent"
                android:layout_height="100dp"
                android:gravity="center"
                android:text="Hello World!">

                <tag
                    android:id="@id/i1"
                    android:value="@color/colorPrimaryDark" />

                <tag
                    android:id="@id/i2"
                    android:value="@color/colorAccent" />
            </Button>

            <TextView
                android:id="@+id/test2"
                daVinCi_bg="@{DaVinCiExpression.shape().corner(`10dp,15dp,20dp,30dp`).stroke(`4dp`,`@i2`).gradient(`#26262a`,`#ff0699`,0)}"
                android:layout_width="match_parent"
                android:layout_height="100dp"
                android:layout_marginTop="10dp"
                android:background="@drawable/test"
                android:gravity="center"
                android:text="@string/app_name">

                <tag
                    android:id="@id/i1"
                    android:value="@color/colorPrimaryDark" />

                <tag
                    android:id="@id/i2"
                    android:value="@color/colorAccent" />
            </TextView>

            <CheckBox
                daVinCi_bg="@{DaVinCiExpression.shape().corner(60).solid(`@i2`).stroke(`4dp`,`@i2`)}"
                daVinCi_bg_pressed="@{DaVinCiExpression.shape().corner(`10dp,15dp,20dp,30dp`).stroke(`4dp`,`@i2`).gradient(`#26262a`,`#ff0699`,0)}"
                android:layout_width="match_parent"
                android:layout_height="100dp"
                android:layout_marginTop="10dp"
                android:background="@drawable/test"
                android:gravity="center"
                android:text="错误示范：daVinCi_bg只能单独使用，一旦有其他的，就需要使用相应的成对的">

                <tag
                    android:id="@id/i1"
                    android:value="@color/colorPrimaryDark" />

                <tag
                    android:id="@id/i2"
                    android:value="@color/colorAccent" />
            </CheckBox>

            <CheckBox
                daVinCi_bg_checked="@{DaVinCiExpression.shape().corner(60).solid(`@i2`).stroke(`4dp`,`@i2`)}"
                daVinCi_bg_unchecked="@{DaVinCiExpression.shape().corner(`10dp,15dp,20dp,30dp`).stroke(`4dp`,`@i2`).gradient(`#26262a`,`#ff0699`,0)}"
                android:layout_width="match_parent"
                android:layout_height="100dp"
                android:layout_marginTop="10dp"
                android:background="@drawable/test"
                android:gravity="center"
                android:text="check状态">

                <tag
                    android:id="@id/i1"
                    android:value="@color/colorPrimaryDark" />

                <tag
                    android:id="@id/i2"
                    android:value="@color/colorAccent" />

                <tag
                    android:id="@id/i3"
                    android:value="@string/app_name" />
            </CheckBox>

        </LinearLayout>

    </androidx.core.widget.NestedScrollView>

</layout>
```
discovery it in the demo!

Considering we may use "resource cleaner", we can use tag to keep the  
 reference of resource and use tagid to access the resource,especially colors, avoid using static string like:"rc/colorPrimaryDark"

考虑到我们会使用资源清理功能，如果使用了"rc/colorPrimaryDark"这种字符串代表颜色资源，可能会被清理掉，
我们可以tag方式去处理，可以保持引用，避免清理，使用"@id/i1"这种形式去访问资源。

不过是否会受到混淆和压缩的影响有待验证，

---

##  APIs:
todo
