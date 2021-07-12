package osp.leobert.android.davinci.meta

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*

public sealed class StyleMetaInfo(
    public val constName: String,
    public val styleName: String,
    public val clzNode: NodeWrapper,
) {

    public fun propertySpec(): PropertySpec {
        return PropertySpec.builder(
            constName, String::class,
            arrayListOf(
                KModifier.CONST, KModifier.PUBLIC
            )
        )
            //            .addAnnotation(JvmStatic::class) is not necessary for const
            .initializer("%S", styleName)
            .build()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StyleMetaInfo

        if (constName != other.constName) return false
        if (styleName != other.styleName) return false
        if (clzNode != other.clzNode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = constName.hashCode()
        result = 31 * result + styleName.hashCode()
        result = 31 * result + clzNode.hashCode()
        return result
    }

    public abstract fun registerBlock(): String

    public class Style(
        constName: String, styleName: String, clzNode: NodeWrapper,
    ) : StyleMetaInfo(
        constName, styleName,
        clzNode
    ) {
        override fun registerBlock(): String {
            val name = clzNode.nodeName()

            return if (name == null) {
                "//could not find name for:$clzNode\r\n"
            } else {
                "osp.leobert.android.davinci.StyleRegistry.register($name())\r\n"
            }
        }

    }

    public class Factory(
        constName: String, styleName: String, clzNode: NodeWrapper,
    ) : StyleMetaInfo(
        constName, styleName,
        clzNode
    ) {
        override fun registerBlock(): String {
            val name = clzNode.nodeName()
            return if (name == null) {
                "//could not find name for:$clzNode\r\n"
            } else {
                "osp.leobert.android.davinci.StyleRegistry.registerFactory($name())\r\n"
            }
        }

    }
}