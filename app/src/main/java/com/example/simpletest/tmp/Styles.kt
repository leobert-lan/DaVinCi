package com.example.simpletest.tmp

/**
 * <p><b>Package:</b> com.example.simpletest.tmp </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> Styles </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/6/17.
 */
object Styles {

    object Text {
        object Btn {
            val main = "text.btn.main"

        }
    }

    @JvmField
    val Text_Btn_test = "btn_style.main"


}

fun main() {
    println(Styles.Text.Btn.main)
}