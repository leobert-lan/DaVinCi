package osp.leobert.android.davinci.preview

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.RequiresApi

/**
 * Created by leobert on 2022/7/20.
 */
class DavinciPreviewInjector : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    init {
        if (isInEditMode) {
            val inflater = LayoutInflater.from(context)
            if (inflater.factory2 == null) {
                DavinciPreviewBHLayoutFactory.originalLayoutInflater = inflater.cloneInContext(inflater.context)
            }
            try {
                inflater.factory2 = DavinciPreviewBHLayoutFactory.instance
            } catch (ignore: Exception) {
            }
        }

    }

    override fun onDraw(canvas: Canvas?) {
        if (isInEditMode) {
            super.onDraw(canvas)
        }
    }

}