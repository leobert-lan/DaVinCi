package osp.leobert.android.davinci

/**
 * <p><b>Package:</b> osp.leobert.android.davinci </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> IStatedExpression </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2022/4/19.
 */
interface IStatedExpression {

    abstract class Visitor {

        companion object {
            val Default = object : Visitor() {
                override fun visit(exp: IStatedExpression): IStatedExpression {
                    return exp
                }
            }
        }

        abstract fun visit(exp: IStatedExpression): IStatedExpression

        open fun visitOther(exp: DaVinCiExpression): IStatedExpression? {
            return null
        }
    }

    fun stateChunksEncode(): Int

    fun isStateGeneralThan(exp: IStatedExpression): Boolean
}