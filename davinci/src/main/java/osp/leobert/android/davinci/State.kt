package osp.leobert.android.davinci

enum class State : StateColorAdapter, StateDrawableAdapter {

    STATE_CHECKABLE_TRUE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setCheckableDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_CHECKABLE_FALSE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setUnCheckableDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_CHECKED_TRUE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setCheckedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_CHECKED_FALSE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setUnCheckedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_ENABLE_TRUE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setEnabledDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },
    STATE_ENABLE_FALSE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setUnEnabledDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_SELECTED_TRUE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setSelectedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },
    STATE_SELECTED_FALSE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setUnSelectedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_PRESSED_TRUE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setPressedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },
    STATE_PRESSED_FALSE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setUnPressedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_FOCUSED_TRUE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setFocusedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    },

    STATE_FOCUSED_FALSE {
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
                daVinCiLoop = daVinCiTemporal, exp = expression, state = this.name, view = daVinCi.view
            )
            daVinCi.core.setUnFocusedDrawable(daVinCiTemporal.core.build())
            daVinCiTemporal.core.clear()
        }
    };
}