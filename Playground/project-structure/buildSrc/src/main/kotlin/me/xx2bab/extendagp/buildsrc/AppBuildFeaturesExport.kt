package me.xx2bab.extendagp.buildsrc

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

fun getAppFeatureSwitchesFromDotKt(android: ApplicationExtension): Map<String, Boolean?>  {
    return mapOf(
        "dataBinding" to android.buildFeatures.dataBinding,
        "mlModelBinding" to android.buildFeatures.mlModelBinding,
        "prefab" to android.buildFeatures.prefab,
        "aidl" to android.buildFeatures.aidl,
        "buildConfig" to android.buildFeatures.buildConfig,
        "compose" to android.buildFeatures.compose,
        "renderScript" to android.buildFeatures.renderScript,
        "shaders" to android.buildFeatures.shaders,
        "resValues" to android.buildFeatures.resValues,
        "viewBinding" to android.buildFeatures.viewBinding
    )
}

fun Project.getAppFeatureSwitchesFromDotKt2(): Map<String, Boolean?>  {
    val android = extensions.getByType<ApplicationExtension>()
    return mapOf(
        "dataBinding" to android.buildFeatures.dataBinding,
        "mlModelBinding" to android.buildFeatures.mlModelBinding,
        "prefab" to android.buildFeatures.prefab,
        "aidl" to android.buildFeatures.aidl,
        "buildConfig" to android.buildFeatures.buildConfig,
        "compose" to android.buildFeatures.compose,
        "renderScript" to android.buildFeatures.renderScript,
        "shaders" to android.buildFeatures.shaders,
        "resValues" to android.buildFeatures.resValues,
        "viewBinding" to android.buildFeatures.viewBinding,
        "caching" to findProperty("org.gradle.caching").toString().toBoolean(),
        "parallel" to findProperty("org.gradle.parallel").toString().toBoolean()
    )
}