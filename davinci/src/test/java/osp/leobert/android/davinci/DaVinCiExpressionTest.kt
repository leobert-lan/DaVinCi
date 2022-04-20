package osp.leobert.android.davinci

import com.google.common.testing.EqualsTester
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 *
 * **Package:** osp.leobert.android.davinci
 *
 * **Project:** DaVinCi
 *
 * **Classname:** DaVinCiExpressionTest
 *
 * **Description:** TODO
 * Created by leobert on 2022/4/20.
 */
@RunWith(JUnit4::class)
class DaVinCiExpressionTest {

    @Test
    fun `isStateGeneralThan$DaVinCi_davinci`() {
        val shape1: DaVinCiExpression = DaVinCiExpression.shape().states(State.CHECKABLE_T)
        val shape2: DaVinCiExpression = DaVinCiExpression.shape().states(State.CHECKABLE_T, State.ENABLE_T)

        val shape3: DaVinCiExpression = DaVinCiExpression.shape().states(State.CHECKABLE_T, State.CHECKED_T)
        val shape4: DaVinCiExpression = DaVinCiExpression.shape().states(State.CHECKABLE_T, State.CHECKED_F)

        val shape5: DaVinCiExpression = DaVinCiExpression.shape().states(State.ENABLE_T)

        val shape6: DaVinCiExpression =
            DaVinCiExpression.shape().states(State.ENABLE_T, State.CHECKED_T, State.PRESSED_T, State.CHECKABLE_T)

        EqualsTester()
            .addEqualityGroup(
                true,
                shape1.isStateGeneralThan(shape3),
                shape1.isStateGeneralThan(shape4),
                shape3.isStateGeneralThan(shape6),
                shape5.isStateGeneralThan(shape6),
                shape1.isStateGeneralThan(shape6),
                shape6.isStateGeneralThan(shape6),
            )
            .addEqualityGroup(
                false,
                shape3.isStateGeneralThan(shape1),
                shape2.isStateGeneralThan(shape5),
            )
            .testEquals()
    }
}