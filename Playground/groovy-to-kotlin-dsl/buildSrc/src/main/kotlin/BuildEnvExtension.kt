
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.newInstance

abstract class BuildEnvExtension(objectFactory: ObjectFactory) {

    val defaultConfig: DefaultConfig = objectFactory.newInstance(DefaultConfig::class.java)

    fun defaultConfig(action: Action<DefaultConfig>) {
        action.execute(defaultConfig)
    }

    fun defaultConfig(action: DefaultConfig.() -> Unit) {
        action.invoke(defaultConfig)
    }
}