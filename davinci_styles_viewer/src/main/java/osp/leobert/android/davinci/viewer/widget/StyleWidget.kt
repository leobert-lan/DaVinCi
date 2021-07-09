package osp.leobert.android.davinci.viewer.widget

import android.graphics.Color
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.BaseObservable
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import osp.leobert.android.davinci.annotation.PreviewConfig
import osp.leobert.android.davinci.annotation.StyleViewer
import osp.leobert.android.davinci.daVinCiBgStyle
import osp.leobert.android.davinci.viewer.R
import osp.leobert.android.davinci.viewer.databinding.AppVhStyleBinding
import osp.leobert.androidkt.pandora.rv.*
import osp.leobert.androidkt.pandora.ui.DataBindingViewHolder
import java.util.*

private val Int.dp: Int
    get() = this.toPx()

private fun Int.toPx(): Int {
    return (this * DisplayMetrics.DENSITY_DEVICE_STABLE + 0.5f).toInt()
}

interface StyleVO2 : DataSet.Data, DataSet.ReactiveData<StyleVO2> {

    val name: String

    var sampleChecked: Boolean

    var sampleEnabled: Boolean

    val width: Int
    val height: Int
    val background: String
    val type: Int


    class Impl(override val name: String, val config: PreviewConfig?) : StyleVO2 {
        private val viewHolders by lazy { ReactiveViewHolders<StyleVO2>() }

        override var sampleChecked: Boolean = false
            set(value) {
                field = value
                viewHolders.notifyPropChanged(this, 1)
            }

        override var sampleEnabled: Boolean = true
            set(value) {
                field = value
                viewHolders.notifyPropChanged(this, 2)
            }

        override val width: Int = config?.width ?: ViewGroup.LayoutParams.MATCH_PARENT

        override val height: Int = config?.height ?: 48.dp

        override val background: String = config?.background ?: "#ffffff"

        override val type: Int = config?.type ?: StyleViewer.FLAG_BG or StyleViewer.FLAG_CSL

        override fun bindReactiveVh(viewHolder: IReactiveViewHolder<StyleVO2>) {
            viewHolders.add(viewHolder)
        }

        override fun unbindReactiveVh(viewHolder: IReactiveViewHolder<StyleVO2>) {
            viewHolders.remove(viewHolder)
        }
    }
}

class StyleVH(val binding: AppVhStyleBinding) :
    DataBindingViewHolder<StyleVO2, AppVhStyleBinding>(binding),
    IReactiveViewHolder<StyleVO2> {

    var mData: StyleVO2? = null

    override fun setData(data: StyleVO2) {
        super.setData(data)
        mData = data
        binding.vo = data

        binding.sample.daVinCiBgStyle(data.name)

        binding.sample.isEnabled = data.sampleEnabled
        binding.sample.isChecked = data.sampleChecked
        binding.sample.text = if (data.type.and(StyleViewer.FLAG_CSL) == StyleViewer.FLAG_CSL) {
            "Sample Area"
        } else {
            ""
        }


        binding.sample.layoutParams.let {
            it.width = data.width
            it.height = data.height
        }

        binding.flSample.setCardBackgroundColor(Color.parseColor(data.background))


        binding.executePendingBindings()
    }

    override fun getReactiveDataIfExist(): DataSet.ReactiveData<StyleVO2>? = mData

    override fun accept(visitor: IViewHolder.Visitor<StyleVO2>) {
        visitor.visit(this)
    }

    override fun onPropertyChanged(sender: Observable?, data: StyleVO2, propertyId: Int) {
        when (propertyId) {
            1 -> binding.sample.isChecked = data.sampleChecked
            2 -> binding.sample.isEnabled = data.sampleEnabled
            else -> {
                setData(data)
                return
            }
        }
        binding.executePendingBindings()
    }

    override val observable: BaseObservable = BaseObservable().apply {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mData?.let { data ->
                    this@StyleVH.onPropertyChanged(sender ?: this@apply, data, propertyId)
                }
            }
        })
    }
}

class StyleVHCreator(private val itemInteract: StyleItemInteract?) : ViewHolderCreator() {

    override fun createViewHolder(parent: ViewGroup): DataBindingViewHolder<StyleVO2, AppVhStyleBinding> {
        val binding = DataBindingUtil.inflate<AppVhStyleBinding>(
            LayoutInflater.from(parent.context),
            R.layout.app_vh_style, parent, false
        )

        val vh = StyleVH(binding)
        binding.vh = vh
        binding.itemInteract = itemInteract

        return vh
    }
}

interface StyleItemInteract {
    //TODO: define interact functions here
}


