import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class VariantTestPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // 5
        project.extensions
            .findByType(AppExtension::class.java)!!
            .buildTypes
            .first()
            .let {
                project.logger.lifecycle("[Pg][DebugTips]: 5 $it.name")
            }
    }

}