package osp.leobert.android.davinci

enum class State(val attr: Int) : StateDrawableAdapter {

    STATE_CHECKABLE_TRUE(android.R.attr.state_checkable) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setCheckableDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_CHECKABLE_FALSE(-android.R.attr.state_checkable) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setUnCheckableDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_CHECKED_TRUE(android.R.attr.state_checked) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setCheckedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_CHECKED_FALSE(-android.R.attr.state_checked) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setUnCheckedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_ENABLE_TRUE(android.R.attr.state_enabled) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setEnabledDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },
    STATE_ENABLE_FALSE(-android.R.attr.state_enabled) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setUnEnabledDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_SELECTED_TRUE(android.R.attr.state_selected) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setSelectedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },
    STATE_SELECTED_FALSE(-android.R.attr.state_selected) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setUnSelectedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_PRESSED_TRUE(android.R.attr.state_pressed) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setPressedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },
    STATE_PRESSED_FALSE(-android.R.attr.state_pressed) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setUnPressedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_FOCUSED_TRUE(android.R.attr.state_focused) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setFocusedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_FOCUSED_FALSE(-android.R.attr.state_focused) {

        override fun adapt(
            daVinCi: DaVinCi,
            daVinCiTemporal: DaVinCi,
            expression: DaVinCiExpression
        ) {
            daVinCiTemporal.core.clear()
            simplify(
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name
            )
            daVinCi.core.setUnFocusedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    };

    override fun toString(): String {
        return name
    }
}