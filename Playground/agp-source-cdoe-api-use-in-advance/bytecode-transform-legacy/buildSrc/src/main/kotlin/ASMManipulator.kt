import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import java.io.File

class ASMManipulator(
    private val inputClassFile: File
) {

    private val reader: ClassReader = ClassReader(inputClassFile.inputStream())
    val writer: ClassWriter =
        ClassWriter(reader, ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)

    fun processInPlace(classVisitor: ClassVisitor) {
        reader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        inputClassFile.writeBytes(writer.toByteArray())
    }

    fun process(classVisitor: ClassVisitor): ByteArray {
        reader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        return writer.toByteArray()
    }

    fun process(classVisitor: ClassVisitor, outputClassFile: File) {
        reader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        outputClassFile.writeBytes(writer.toByteArray())
    }

}
