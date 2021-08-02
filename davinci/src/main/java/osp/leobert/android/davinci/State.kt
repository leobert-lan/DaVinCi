package osp.leobert.android.davinci

enum class State(val attr: Int) {

    @Deprecated("too long",replaceWith = ReplaceWith("CHECKABLE_T"))
    STATE_CHECKABLE_TRUE(android.R.attr.state_checkable),
    @Deprecated("too long",replaceWith = ReplaceWith("CHECKABLE_F"))
    STATE_CHECKABLE_FALSE(-android.R.attr.state_checkable),

    @Deprecated("too long",replaceWith = ReplaceWith("CHECKED_T"))
    STATE_CHECKED_TRUE(android.R.attr.state_checked),
    @Deprecated("too long",replaceWith = ReplaceWith("CHECKED_F"))
    STATE_CHECKED_FALSE(-android.R.attr.state_checked),

    @Deprecated("too long",replaceWith = ReplaceWith("ENABLE_T"))
    STATE_ENABLE_TRUE(android.R.attr.state_enabled),
    @Deprecated("too long",replaceWith = ReplaceWith("ENABLE_F"))
    STATE_ENABLE_FALSE(-android.R.attr.state_enabled),

    @Deprecated("too long",replaceWith = ReplaceWith("SELECTED_T"))
    STATE_SELECTED_TRUE(android.R.attr.state_selected),
    @Deprecated("too long",replaceWith = ReplaceWith("SELECTED_F"))
    STATE_SELECTED_FALSE(-android.R.attr.state_selected),

    @Deprecated("too long",replaceWith = ReplaceWith("PRESSED_T"))
    STATE_PRESSED_TRUE(android.R.attr.state_pressed),
    @Deprecated("too long",replaceWith = ReplaceWith("PRESSED_F"))
    STATE_PRESSED_FALSE(-android.R.attr.state_pressed),

    @Deprecated("too long",replaceWith = ReplaceWith("FOCUSED_T"))
    STATE_FOCUSED_TRUE(android.R.attr.state_focused),
    @Deprecated("too long",replaceWith = ReplaceWith("FOCUSED_F"))
    STATE_FOCUSED_FALSE(-android.R.attr.state_focused),

    //region 新

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

    //endregion

    override fun toString(): String {
        return name
    }
}