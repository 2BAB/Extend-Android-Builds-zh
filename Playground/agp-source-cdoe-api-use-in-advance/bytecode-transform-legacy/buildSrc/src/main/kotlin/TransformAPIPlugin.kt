
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.QualifiedContent.DefaultContentType
import com.android.build.api.transform.QualifiedContent.Scope
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.BaseExtension
import com.android.utils.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import java.io.File

class TransformAPIPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // val androidComponents = project.extensions
        //     .getByType(ApplicationAndroidComponentsExtension::class.java)
        val androidLegacyExtension = project.extensions.getByType<BaseExtension>()
        androidLegacyExtension.registerTransform(ClassReplacerTransform())
    }

}

class ClassReplacerTransform : Transform() {
    override fun getName() = "ClassReplacerTransform"

    override fun getInputTypes(): Set<ContentType> {
        return setOf(
            DefaultContentType.CLASSES,
            //DefaultContentType.RESOURCES
        )
        // return TransformManager.CONTENT_JARS
    }

    override fun getScopes(): MutableSet<in Scope> {
        return mutableSetOf(Scope.PROJECT, Scope.SUB_PROJECTS, Scope.EXTERNAL_LIBRARIES)
        // return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(invocation: TransformInvocation) {
        super.transform(invocation)
        invocation.outputProvider.deleteAll()
        // this.parameterInputs
        invocation.inputs.forEach { input ->
            input.directoryInputs.forEach { inputDir ->
                val destDir = invocation.outputProvider.getContentLocation(
                    inputDir.name,
                    inputDir.contentTypes,
                    inputDir.scopes,
                    Format.DIRECTORY
                )
                destDir.mkdirs()
                for (perFile in FileUtils.getAllFiles(inputDir.file)) {
                    val destFile = File(destDir, perFile.name)
                        .also { it.exists() || it.createNewFile() }
                    if (perFile.extension == "class") {
                        processClass(perFile, destFile)
                    } else {
                        FileUtils.copyFile(perFile, destFile)
                    }
                }
            }
            input.jarInputs.forEach { jarInput ->
                val outputJar = invocation.outputProvider.getContentLocation(
                    jarInput.name,
                    jarInput.contentTypes,
                    jarInput.scopes,
                    Format.JAR
                )
                // skip the process since we do not need it in the sample...
                jarInput.file.copyTo(outputJar)
            }
        }
    }

    private fun processClass(inputFile: File, outputFile: File) {
        val manipulator = ASMManipulator(inputFile)
        manipulator.process(ReplaceClassVisitor(manipulator.writer), outputFile)
    }

}