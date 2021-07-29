package osp.leobert.android.davinci

enum class State(val attr:Int) : StateColorAdapter, StateDrawableAdapter {

    STATE_CHECKABLE_TRUE(android.R.attr.state_checkable) {
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setCheckableTextColor(colorInt)
        }

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
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setUnCheckableTextColor(colorInt)
        }

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
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setCheckedTextColor(colorInt)
        }

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
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setUnCheckedTextColor(colorInt)
        }

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
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setEnabledTextColor(colorInt)
        }

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
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setUnEnabledTextColor(colorInt)
        }

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
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setSelectedTextColor(colorInt)
        }

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
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setUnSelectedTextColor(colorInt)
        }

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
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setPressedTextColor(colorInt)
        }

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
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setUnPressedTextColor(colorInt)
        }

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
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setFocusedTextColor(colorInt)
        }

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
        override fun adapt(core: DaVinCiCore, colorInt: Int) {
            core.setUnFocusedTextColor(colorInt)
        }

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