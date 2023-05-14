import com.android.build.api.dsl.ApplicationExtension

extra["getAppFeatureSwitchesFromScriptPlugin"] = fun(): Map<String, Boolean?> {
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
        "caching" to project.findProperty("org.gradle.caching").toString().toBoolean(),
        "parallel" to project.findProperty("org.gradle.parallel").toString().toBoolean()
    )
}