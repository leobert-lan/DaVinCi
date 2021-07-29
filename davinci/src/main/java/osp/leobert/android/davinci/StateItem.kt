package osp.leobert.android.davinci

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.Log
import androidx.annotation.ColorInt
import kotlin.math.absoluteValue

/**
 * <p><b>Package:</b> osp.leobert.android.davinci </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> StateItem </p>
 * <p><b>Description:</b> item for [ColorStateList] or [StateListDrawable] ,
 * used to build [ColorStateList] or [StateListDrawable] in android</p>
 * Created by leobert on 7/28/21.
 */
sealed class StateItem<T:StateItem<T>> {

    companion object {
        fun of(@ColorInt colorInt: Int): ColorItem = ColorItem(colorInt)

        fun of(drawable: Drawable): DrawableItem = DrawableItem(drawable)
    }

    private val states: ArrayList<State> = arrayListOf()

    fun applyState(states: List<State>): T {
        for (state in states) {
            this.states.takeUnless { it.contains(state) }?.add(state)
        }
        val hasOppositeStates = states.size != states.map { it.attr.absoluteValue }.toCollection(hashSetOf()).size
        if (hasOppositeStates)
            Log.e(DaVinCiExpression.sLogTag,"has opposite states:${states.joinToString()}")
        return this.getT()
    }

    protected abstract fun getT():T

    fun getStatesArray(): IntArray {
        return states.sortedBy { it.attr.absoluteValue }.map { it.attr }.toIntArray()
    }


    class ColorItem(@ColorInt val colorInt: Int) : StateItem<ColorItem>() {
        override fun getT(): ColorItem {
            return this
        }
    }

    class DrawableItem(val drawable: Drawable) : StateItem<DrawableItem>() {
        override fun getT(): DrawableItem {
            return this
        }
    }

}