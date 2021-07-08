package osp.leobert.android.davinci.viewer.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import osp.leobert.android.davinci.viewer.databinding.AppVhStyleBinding
import osp.leobert.android.davinci.viewer.R
import java.util.*

import androidx.databinding.Observable
import androidx.databinding.BaseObservable

import osp.leobert.androidkt.pandora.rv.*
import osp.leobert.androidkt.pandora.ui.DataBindingViewHolder

interface StyleVO2 : DataSet.Data, DataSet.ReactiveData<StyleVO2> {
    class Impl : StyleVO2 {
        private val viewHolders by lazy { ReactiveViewHolders<StyleVO2>() }

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
        binding.vh = this
        binding.vo = data
        binding.executePendingBindings()
    }

    override fun getReactiveDataIfExist(): DataSet.ReactiveData<StyleVO2>? = mData

    override fun accept(visitor: IViewHolder.Visitor<StyleVO2>) {
        visitor.visit(this)
    }

    override fun onPropertyChanged(sender: Observable?, data: StyleVO2, propertyId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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


