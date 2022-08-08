package osp.leobert.android.davinci.preview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.InjectorUtils
import androidx.databinding.ViewDataBinding

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.preview </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DavinciPreviewBHLayoutFactory </p>
 * Created by leobert on 2022/8/8.
 */
class DavinciPreviewBHLayoutFactory private constructor() : LayoutInflater.Factory2 {

    companion object {
        var originalLayoutInflater: LayoutInflater? = null
        val instance = DavinciPreviewBHLayoutFactory()
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return createView(parent, name, context, attrs)
    }


    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return createView(null, name, context, attrs)
    }

    var holder: ViewGroup? = null

    var previewId: Int = 0

    private fun createView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        if (DavinciPreviewInjector::class.java.name == name) {

            return DavinciPreviewInjector(context, attrs)
        }

        if (PreviewHolder::class.java.name == name) {
            val ret = PreviewHolder(context, attrs).apply {
                val a = context.obtainStyledAttributes(
                    attrs, R.styleable.davinci, 0, 0
                )
                previewId = a.getResourceId(R.styleable.davinci_previewId, 0)
                if (previewId == 0) {
                    previewId =
                        context.resources.getIdentifier(a.getString(R.styleable.davinci_previewName)!!, "layout", context.packageName)
                }
                a.recycle()
            }
            holder = ret
            if (previewId == 0) throw NullPointerException("no preview id")

            val holder = holder ?: throw NullPointerException("previewHolder is null")
            holder.removeAllViews()

            val inflater = originalLayoutInflater!!.cloneInContext(originalLayoutInflater!!.context)
            inflater.factory2 = object : LayoutInflater.Factory2 {
                override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
                    return originalLayoutInflater!!.createView(name, "android.view", attrs)?.apply {
                        handleTag(this, attrs)
                    }
                }

                override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                    return originalLayoutInflater!!.createView(name, "android.view", attrs)?.apply {
                        handleTag(this, attrs)
                    }
                }

                var bindingIndex = 0

                fun handleTag(view: View, attrs: AttributeSet) {
                    val a = context.obtainStyledAttributes(
                        attrs, R.styleable.davinci, 0, 0
                    )
                    if (view.tag?.toString()?.startsWith("layout") == true) {
                        bindingIndex = 1
                        a.recycle()
                        return
                    }

                    if (a.getBoolean(R.styleable.davinci_binding_mark, false)) {
                        view.tag = "binding_$bindingIndex"
                        bindingIndex++
                    }

                    a.recycle()
                }
            }

            val binding = DataBindingUtil.inflate<ViewDataBinding>(
                inflater,
                previewId, holder, true
            )

            InjectorUtils.refreshBinding(binding)
            return ret
        }
        return null
    }
}