package osp.leobert.android.davinci.annotation

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.annotation </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DavinciStyle </p>
 * notate osp.leobert.android.davinci.StyleRegistry.Style
 * Created by leobert on 2021/6/15.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
public annotation class DaVinCiStyle(val styleName: String)
