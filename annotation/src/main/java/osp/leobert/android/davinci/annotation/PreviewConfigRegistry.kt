package osp.leobert.android.davinci.annotation

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.annotation </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> PreviewConfigRegistry </p>
 * Created by leobert on 2021/7/9.
 */
public object PreviewConfigRegistry {

    private val configs = hashMapOf<String, PreviewConfig>()

    public fun clear() {
        configs.clear()
    }

    public fun register(name: String, config: PreviewConfig) {
        configs[name] = config
    }

    public fun find(name:String):PreviewConfig? {
        return configs[name]
    }

}