package osp.leobert.android.davinci

enum class State(val attr: Int) {

    STATE_CHECKABLE_TRUE(android.R.attr.state_checkable),

    STATE_CHECKABLE_FALSE(-android.R.attr.state_checkable),

    STATE_CHECKED_TRUE(android.R.attr.state_checked),

    STATE_CHECKED_FALSE(-android.R.attr.state_checked),

    STATE_ENABLE_TRUE(android.R.attr.state_enabled),
    STATE_ENABLE_FALSE(-android.R.attr.state_enabled),

    STATE_SELECTED_TRUE(android.R.attr.state_selected),
    STATE_SELECTED_FALSE(-android.R.attr.state_selected),

    STATE_PRESSED_TRUE(android.R.attr.state_pressed),
    STATE_PRESSED_FALSE(-android.R.attr.state_pressed),

    STATE_FOCUSED_TRUE(android.R.attr.state_focused),

    STATE_FOCUSED_FALSE(-android.R.attr.state_focused);

    override fun toString(): String {
        return name
    }
}