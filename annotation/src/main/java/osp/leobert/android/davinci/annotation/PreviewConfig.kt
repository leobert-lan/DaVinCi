package osp.leobert.android.davinci.annotation

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.annotation </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> PreviewConfig </p>
 * Created by leobert on 2021/7/9.
 */
public class PreviewConfig(
    public val width: Int,
    public val height: Int,
    public val background: String,
    public val type: Int,
) {

    public val isBackground: Boolean = type.and(StyleViewer.FLAG_BG) == StyleViewer.FLAG_BG

    public val isCsl: Boolean = type.and(StyleViewer.FLAG_CSL) == StyleViewer.FLAG_CSL

    override fun toString(): String {
        return "PreviewConfig(width=$width, height=$height, background='$background', type=$type)"
    }
}