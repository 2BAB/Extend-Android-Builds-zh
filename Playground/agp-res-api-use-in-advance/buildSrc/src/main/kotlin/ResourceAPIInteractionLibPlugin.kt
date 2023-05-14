import com.android.build.api.artifact.MultipleArtifact
import com.android.build.api.variant.*
import com.android.build.api.variant.impl.ResValueKeyImpl
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.register
import java.io.File

class ResourceAPIInteractionLibPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val libExtension = project.extensions.getByType(
            LibraryAndroidComponentsExtension::class.java
        )

        libExtension.onVariants { variant ->
            project.afterEvaluate {
                project.tasks
                    .named("bundle${variant.name.capitalize()}Aar")
                    .configure {
                        // abstract class BundleAar : Zip(), VariantAwareTask
                        val t = this as com.android.build.gradle.tasks.BundleAar
                        t.from(file("lib-appended-file-at-aar-root.txt"))
                    }
            }
        }
    }

}

