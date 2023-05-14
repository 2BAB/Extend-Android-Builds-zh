import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class BuildEnvStubPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        println("BuildEnvStubPlugin applied")
        val ext = target.extensions.create<BuildEnvExtension>("buildEnv")
        println("BuildEnvExtension created")
        target.afterEvaluate {
//            println("BuildEnvExtension message: " + ext.defaultConfig.message.get())
        }
    }
}