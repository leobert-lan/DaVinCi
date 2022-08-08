package androidx.databinding

/**
 * Classname: InjectorUtils </p>
 * Created by leobert on 2022/8/2.
 */
object InjectorUtils {
    fun refreshBinding(binding: ViewDataBinding) {
        binding.invalidateAll()
        binding.forceExecuteBindings()
    }
}