import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationParameters
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import me.xx2bab.bytecode.intro.ReplaceClassVisitor
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor

class InstrumentationAPIPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val androidComponents =
            project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
        androidComponents.onVariants { appVariant ->
            appVariant.instrumentation
                .transformClassesWith(
                    WakeLockClassVisitorFactory::class.java,
                    InstrumentationScope.ALL
                ) {}
            appVariant.instrumentation
                .setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
    }
}

abstract class WakeLockClassVisitorFactory :
    AsmClassVisitorFactory<InstrumentationParameters.None> {

    override fun isInstrumentable(classData: ClassData): Boolean {
        return classData.className
            .startsWith("me.xx2bab.bytecode.intro")
            .not()
    }

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return ReplaceClassVisitor(nextClassVisitor)
    }

}
