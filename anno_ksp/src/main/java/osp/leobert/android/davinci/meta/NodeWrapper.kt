package osp.leobert.android.davinci.meta

import com.google.devtools.ksp.symbol.KSClassDeclaration
import javax.lang.model.element.Element

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.meta </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> NodeWrapper </p>
 * Created by leobert on 2021/7/12.
 */
public abstract class NodeWrapper {

    public abstract fun nodeName(): String?

    abstract override fun toString(): String

    public class KtNodeWrapper(public val clzNode: KSClassDeclaration) : NodeWrapper() {
        override fun nodeName(): String? {
            return clzNode.qualifiedName?.asString()
        }

        override fun toString(): String {
            return clzNode.toString()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as KtNodeWrapper

            if (clzNode != other.clzNode) return false

            return true
        }

        override fun hashCode(): Int {
            return clzNode.hashCode()
        }
    }


    public class JavaNodeWrapper(public val clzNode: Element) : NodeWrapper() {
        override fun nodeName(): String {
            return clzNode.asType().toString()
        }

        override fun toString(): String {
            return clzNode.toString()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as KtNodeWrapper

            if (clzNode != other.clzNode) return false

            return true
        }

        override fun hashCode(): Int {
            return clzNode.hashCode()
        }
    }

}