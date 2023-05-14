import com.github.javaparser.Providers.provider
import me.xx2bab.extendagp.buildlogic.BuildFinishTrackService
import me.xx2bab.gradle.lifecycle.build.Lifecycle

Lifecycle.onEvaluatingSettingsScript(
    "This message placed before and out of the buildscipt{} " +
            "block, But it STILL prints after the message that in buildscipt{} block."
)

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.namespace) {
                "com.android" ->
                    useModule("com.android.tools.build:gradle:7.2.2")

                "org.jetbrains.kotlin" ->
                    useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
            }
        }
    }
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    includeBuild("./build-logic")
}
plugins {
    id("lifecycle-settings")
    id("lifecycle-listener-registry")
}


//buildscript {
//    println("[Lifecycle] >>> [onEvaluatingSettingsScript] >>> " +
//            "Hi! Everybody! Let buildscript{} get evaluated first! " +
//            "Noticed that, here inside the buildscript{} can not invoke " +
//            "`Lifecycle#onEvaluatingSettingsScript` that is from `buildSrc` component.")
//
//    println("[Lifecycle] >>> [onEvaluatingSettingsScript] >>> " +
//            ">>> The classloader of buildscript is $classLoader.")
//
//    repositories {
//        mavenCentral()
//    }
//    dependencies {
//        classpath("com.squareup.okhttp3:okhttp:4.10.0")
//    }
//}

rootProject.children.forEach { project ->
    project.buildFileName = "${project.name}.gradle.kts"
}
Lifecycle.onEvaluatingSettingsScript(
    "Of course, you can reference anything from buildSrc in " +
            "settings.gradle. For example, we can get the value of `targetSDK` from `Config.kt`: " +
            IncludeBuildConfig.Android.targetSDK
)
Lifecycle.onEvaluatingSettingsScript(
    "However, dependencies you defined above can only work " +
            "in this scripts, or classpath scope is limited. Here is a snippet shows the usage" +
            "of the lib `okhttp`:"
)
Lifecycle.onEvaluatingSettingsScript(
    "The classloader of settings is ${javaClass.classLoader}," +
            " hashcode is ${javaClass.classLoader.hashCode()}."
)

include(":app", ":lib")

Lifecycle.onEvaluatingSettingsScript("Usually we write our scripts here after including modules.")

rootProject.children.forEach {
    it.buildFileName = "${it.name}.gradle.kts"
}


// println("rootProject.children.size: " + rootProject.children.size)

gradle.settingsEvaluated {
    Lifecycle.onSettingsEvaluated("Here you can refer all ProjectDescriptors, ex: ")
    // ${this.project(":app").name}

    // To help find invoking stack from the source code
    val eles = Thread.currentThread().stackTrace
    eles.forEach {
        println(it.toString())
    }
}

gradle.projectsLoaded {
    rootProject.beforeEvaluate {
        Lifecycle.beforeEvaluate(this.displayName)
    }

    rootProject.tasks.whenTaskAdded {
        Lifecycle.whenTaskAdded(rootProject.name + ":" + this.name)
    }
}

gradle.beforeProject {
    Lifecycle.beforeProject(this.displayName)
}

gradle.afterProject {
    Lifecycle.afterProject(this.displayName)
}

gradle.projectsEvaluated {
    Lifecycle.onProjectsEvaluated("")
}

gradle.taskGraph.whenReady {
    Lifecycle.whenTaskGraphIsReady("whenReady")
}

//gradle.addListener(object : TaskExecutionGraphListener {
//    override fun graphPopulated(graph: TaskExecutionGraph) {
//        Lifecycle.whenTaskGraphIsReady("graphPopulated")
//    }
//})
//
//gradle.addBuildListener(object :BuildListener {
//    override fun settingsEvaluated(settings: Settings) {
//        Lifecycle.onSettingsEvaluated("Trigger by BuildListener")
//    }
//
//    override fun projectsLoaded(gradle: Gradle) {
//        Lifecycle.onProjectsLoaded("Trigger by BuildListener")
//    }
//
//    override fun projectsEvaluated(gradle: Gradle) {
//        Lifecycle.onProjectsEvaluated("Trigger by BuildListener")
//    }
//
//    override fun buildFinished(result: BuildResult) {
//        Lifecycle.onBuildFinished("BuildListener")
//    }
//})

/************ Deprecated APIs ************/
//gradle.taskGraph.beforeTask {
//    Lifecycle.beforeTask(this.name)
//}
//
//gradle.taskGraph.afterTask {
//    Lifecycle.afterTask(this.name)
//}
//
//gradle.addListener(object : TaskExecutionListener {
//    override fun beforeExecute(task: Task) {
//        Lifecycle.beforeTask("TaskExecutionListener >>> ${task.name}")
//    }
//
//    override fun afterExecute(task: Task, state: TaskState) {
//        Lifecycle.afterTask("TaskExecutionListener >>> ${task.name}")
//    }
//})
//
//// Same as above TaskExecutionListener.
//// This adapter should be deprecated as well.
//gradle.addListener(object : TaskExecutionAdapter() {
//    override fun beforeExecute(task: Task) {
//    }
//
//    override fun afterExecute(task: Task, state: TaskState) {
//    }
//})
//
//gradle.addListener(object : TaskActionListener {
//    override fun beforeActions(task: Task) {
//        Lifecycle.beforeAction(task.name)
//    }
//
//    override fun afterActions(task: Task) {
//        Lifecycle.afterAction(task.name)
//    }
//})
//
//gradle.buildFinished {
//    Lifecycle.onBuildFinished("gradle.buildFinished")
//}
/************ Deprecated APIs ************/


/************ Replaced APIs ************/
// See the plugin above. `plugins { id("lifecycle-listener-registry") }`

/************ Replaced APIs ************/
