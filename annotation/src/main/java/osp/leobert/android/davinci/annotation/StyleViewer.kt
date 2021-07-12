package osp.leobert.android.davinci.annotation


/**
 * <p><b>Package:</b> osp.leobert.android.davinci.annotation </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> StyleViewer </p>
 * <p><b>Description:</b> 演示Style时的参数 </p>
 * Created by leobert on 2021/6/24.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
public annotation class StyleViewer(
    val height: Int = 48,
    val width: Int = -1 /*android.view.ViewGroup.LayoutParams.MATCH_PARENT = -1*/,
    val background: String = "#ffffff",
    val type: Int = FLAG_BG or FLAG_CSL,
) {
    public companion object {
        public const val FLAG_BG: Int = 1 shl 0
        public const val FLAG_CSL: Int = 1 shl 1
    }
}
