package osp.leobert.android.davinci.meta

import com.google.devtools.ksp.symbol.KSClassDeclaration

public class PreviewConfigMetaInfo(
    public val styleName: String,
    public val clzNode: KSClassDeclaration,
    public val width: Int,
    public val height: Int,
    public val background: String,
    public val type: Int,
) {
    public fun registerBlock(): String {
        // public val width: Int,
        //    public val height: Int,
        //    public val background: String,
        //    public val type: Int,
        return "osp.leobert.android.davinci.annotation.PreviewConfigRegistry.register(" +
                "name = \"$styleName\", " +
                "config = osp.leobert.android.davinci.annotation.PreviewConfig(" +
                "width = $width, " +
                "height = $height, " +
                "background = \"$background\", " +
                "type = $type" +
                ")" +
                ")\r\n"

    }

}