package osp.leobert.android.davinci

internal interface StateColorAdapter {

    // TODO: 2021/7/21 不应当是DaVinciCore ，应该是对应一个item的类，
    fun adapt(core: DaVinCiCore, colorInt: Int)
}