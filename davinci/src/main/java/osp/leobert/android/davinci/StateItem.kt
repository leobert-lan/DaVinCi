package osp.leobert.android.davinci

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt

/**
 * <p><b>Package:</b> osp.leobert.android.davinci </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> StateItem </p>
 * <p><b>Description:</b> item for [ColorStateList] or [StateListDrawable] </p>
 * Created by leobert on 7/28/21.
 */
sealed class StateItem() {

    companion object {
        fun of(@ColorInt colorInt: Int): ColorItem = ColorItem(colorInt)

        fun of(drawable: Drawable):DrawableItem = DrawableItem(drawable)
    }

    private val states: ArrayList<State> = arrayListOf()

    fun applyState(vararg states: State):StateItem {
        for (state in states) {
            this.states.takeUnless { it.contains(state) }?.add(state)
        }
        //todo consider pair check? e.g. android.R.attr.state_enabled -android.R.attr.state_enabled
        return this
    }





    class ColorItem(@ColorInt val colorInt: Int) {

    }

    class DrawableItem(val drawable: Drawable) {

    }

}