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
    val height: Int = 40,
    val width: Int = 100,
    val background: String = "#00000000",
)
