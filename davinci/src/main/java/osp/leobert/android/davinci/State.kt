package osp.leobert.android.davinci

enum class State(val attr: Int, val tag: Int) {

    CHECKABLE_T(android.R.attr.state_checkable, 0x01 shl 0),
    CHECKABLE_F(-android.R.attr.state_checkable, 0x01 shl 1),

    CHECKED_T(android.R.attr.state_checked, 0x01 shl 2),
    CHECKED_F(-android.R.attr.state_checked, 0x01 shl 3),

    ENABLE_T(android.R.attr.state_enabled, 0x01 shl 4),
    ENABLE_F(-android.R.attr.state_enabled, 0x01 shl 5),

    SELECTED_T(android.R.attr.state_selected, 0x01 shl 6),
    SELECTED_F(-android.R.attr.state_selected, 0x01 shl 7),

    PRESSED_T(android.R.attr.state_pressed, 0x01 shl 8),
    PRESSED_F(-android.R.attr.state_pressed, 0x01 shl 9),

    FOCUSED_T(android.R.attr.state_focused, 0x01 shl 10),
    FOCUSED_F(-android.R.attr.state_focused, 0x01 shl 11);

    override fun toString(): String {
        return name
    }

    fun appendEncode(encode:Int) :Int{
        return encode or tag
    }

    companion object {
        @JvmStatic
        fun array(vararg state: State) = state
    }

}