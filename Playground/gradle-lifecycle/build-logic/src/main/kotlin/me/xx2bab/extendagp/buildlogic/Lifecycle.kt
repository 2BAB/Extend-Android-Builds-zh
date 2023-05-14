package me.xx2bab.gradle.lifecycle.build

object Lifecycle {

    fun println(text: String) {
        kotlin.io.println("[Lifecycle] >>> $text")
    }

    fun onBuildStarted() {
        throw IllegalAccessException(
            "Can not trigger this hook" +
                    "since it only works in Gradle Framework inside."
        )
    }

    fun onEvaluatingInitScript() {
        throw IllegalAccessException(
            "Can not trigger this hook" +
                    "since init.gradle didn't depend on buildSrc."
        )
    }

    fun onEvaluatingSettingsScript(message: String) {
        println("[onEvaluatingSettingsScript] >>> $message")
    }

    fun onSettingsEvaluated(message: String) {
        println("[onSettingsEvaluated] >>> $message")
    }

    fun onProjectsLoaded(message: String) {
        println("[onProjectsLoaded] >>> $message")
    }

    fun beforeProject(message: String) {
        println("[beforeProject] >>> $message")
    }

    fun beforeEvaluate(message: String) {
        println("[beforeEvaluate] >>> $message")
    }

    fun onEvaluatingProjectWithinBuildScript(message: String) {
        println("[onEvaluatingProjectWithinBuildScript] >>> $message")
    }

    fun afterProject(message: String) {
        println("[afterProject] >>> $message")
    }

    fun afterEvaluate(message: String) {
        println("[afterEvaluate] >>> $message")
    }

    fun onProjectsEvaluated(message: String) {
        println("[onProjectsEvaluated] >>> $message")
    }

    fun whenTaskAdded(message: String) {
        println("[whenTaskAdded] >>> $message")
    }

    fun whenTaskGraphIsReady(message: String) {
        println("[whenTaskGraphIsReady] >>> $message")
    }

    fun beforeTask(message: String) {
        println("[beforeTask] >>> $message")
    }

    fun beforeAction(message: String) {
        println("[beforeAction] >>> $message")
    }

    fun afterAction(message: String) {
        println("[afterAction] >>> $message")
    }

    fun afterTask(message: String) {
        println("[afterTask] >>> $message")
    }

    fun onBuildFinished(message: String) {
        println("[onBuildFinished] >>> $message")
    }

}