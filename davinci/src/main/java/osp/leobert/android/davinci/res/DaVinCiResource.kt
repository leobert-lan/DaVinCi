package osp.leobert.android.davinci.res

import androidx.annotation.ColorInt

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.res </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DaVinCiResource </p>
 * Created by leobert on 2022/4/14.
 */
sealed class DaVinCiResource {

    class ColorIntRes(@ColorInt val colorInt: Int?) : DaVinCiResource()
}
