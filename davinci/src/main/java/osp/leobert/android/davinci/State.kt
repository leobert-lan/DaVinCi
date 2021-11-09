package osp.leobert.android.davinci

enum class State(val attr: Int) {

    CHECKABLE_T(android.R.attr.state_checkable),
    CHECKABLE_F(-android.R.attr.state_checkable),

    CHECKED_T(android.R.attr.state_checked),
    CHECKED_F(-android.R.attr.state_checked),

    ENABLE_T(android.R.attr.state_enabled),
    ENABLE_F(-android.R.attr.state_enabled),

    SELECTED_T(android.R.attr.state_selected),
    SELECTED_F(-android.R.attr.state_selected),

    PRESSED_T(android.R.attr.state_pressed),
    PRESSED_F(-android.R.attr.state_pressed),

    FOCUSED_T(android.R.attr.state_focused),
    FOCUSED_F(-android.R.attr.state_focused);

    override fun toString(): String {
        return name
    }

    companion object {
        @JvmStatic
        fun array(vararg state: State) = state
    }

}