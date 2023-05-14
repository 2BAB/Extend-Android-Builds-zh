import com.android.build.api.variant.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class VariantV2BasisPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidExtension = project.extensions
            .findByType(ApplicationAndroidComponentsExtension::class.java)!!

        androidExtension.finalizeDsl { appExt ->
            appExt.buildFeatures.dataBinding = false
            appExt.buildFeatures.viewBinding = true
            appExt.buildTypes.getByName("debug").isDebuggable = false
            appExt.productFlavors.getByName("staging").minSdk = 30
        }

        androidExtension.beforeVariants { variantBuilder ->
            variantBuilder.minSdk = 30
            variantBuilder.registerExtension(
                NameGenerator::class.java,
                NameGenerator(variantBuilder.name)
            )
        }

        androidExtension.beforeVariants(
            androidExtension.selector()
                .withBuildType("debug")
                .withFlavor(Pair("server", "production"))
        ) { variantBuilder ->
            variantBuilder.enabled = false
        }

        androidExtension.onVariants { variant ->
            // Configurations
            project.logger.lifecycle("variant name: ${variant.name}")
            // project.logger.lifecycle("variant.applicationId: ${variant.applicationId}")
            // project.logger.lifecycle("variant.applicationId: ${variant.namespace.get()}")
            // project.logger.lifecycle("variant.versionCode: ${mainOutput.versionCode.get()}")
            project.logger.lifecycle("variant.productFlavors: ${variant.productFlavors.size}")

            val taskNameSuffix = variant.getExtension(NameGenerator::class.java)!!
                .taskNameSuffix
            project.logger.lifecycle("variant taskNamePrefix: $taskNameSuffix")
            // Task Providers are removed from new variant APIs.
        }


        // Advanced Variant APIs, to update merged flavors config
        androidExtension.onVariants(
            androidExtension
                .selector()
                .withBuildType("release")
                .withFlavor(Pair("server", "production"))
        ) { variant ->
            val mainOutput: VariantOutput = variant.outputs.single {
                it.outputType == VariantOutputConfiguration.OutputType.SINGLE
            }
            mainOutput.versionName.set("1.1.0")
            variant.androidResources.aaptAdditionalParameters.add("-v")
//             variant.signingConfig?.setConfig(...)
        }

    }

}

class NameGenerator(private val variantName: String) {
    val taskNameSuffix = "V2Based" + variantName.capitalize()
}