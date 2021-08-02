package osp.leobert.android.davinci

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat

/**
 * <p><b>Package:</b> osp.leobert.android.davinci </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> Applier </p>
 * Created by leobert on 2021/7/30.
 */
interface Applier {
    val context: Context

    fun getTag(id: Int): Any?

    fun applyDrawable(drawable: Drawable?)

    fun applyColorStateList(csl: ColorStateList?)

    abstract class Adapter(override val context: Context) : Applier {
        override fun getTag(id: Int): Any? {
            return null
        }

        override fun applyDrawable(drawable: Drawable?) {
        }

        override fun applyColorStateList(csl: ColorStateList?) {
        }
    }

    open class Composer(context: Context, var drawableConsumer: (Drawable?.() -> Unit)?, var cslConsumer: (ColorStateList?.() -> Unit)?) :
        Adapter(context) {
        override fun applyDrawable(drawable: Drawable?) {
            drawableConsumer?.invoke(drawable)
        }

        override fun applyColorStateList(csl: ColorStateList?) {
            cslConsumer?.invoke(csl)
        }
    }

    class ViewComposer<T : View>(val view: T) : Composer(context = view.context, null, null) {
        override fun getTag(id: Int): Any? {
            return view.getTag(id)
        }
    }

    companion object {
        inline fun drawable(context: Context, crossinline consumer: Drawable?.() -> Unit) = object : Adapter(context) {
            override fun applyDrawable(drawable: Drawable?) {
                consumer.invoke(drawable)
            }
        }

        inline fun csl(context: Context, crossinline consumer: ColorStateList?.() -> Unit) = object : Adapter(context) {
            override fun applyColorStateList(csl: ColorStateList?) {
                consumer.invoke(csl)
            }
        }

        fun View.viewBackground() = ViewComposer<View>(this).apply {
            this.drawableConsumer = { ViewCompat.setBackground(this@apply.view, this) }
        }

        fun <T : View> ViewComposer<T>.viewBackground(): ViewComposer<T> {
            this.drawableConsumer = { ViewCompat.setBackground(this@viewBackground.view, this) }
            return this
        }

        fun TextView.csl() = ViewComposer<TextView>(this).apply {
            this.cslConsumer = { this@apply.view.setTextColor(this) }
        }

        fun View.applier(): Applier {
            return when (this) {
                is TextView -> this.csl().viewBackground()
                else -> this.viewBackground()
            }
        }
    }
}