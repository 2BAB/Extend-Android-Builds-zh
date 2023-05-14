package me.xx2bab.bytecode.intro

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class ReplaceClassVisitor(classWriter: ClassVisitor) : ClassVisitor(Opcodes.ASM9, classWriter) {

    private var tempClassName = ""

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        // println("visitClass $name")
        tempClassName = name ?: ""
    }

    override fun visitMethod(
        access: Int,
        methodName: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val sv = super.visitMethod(access, methodName, descriptor, signature, exceptions)
        return WakeLockReplaceMethodVisitor(access, descriptor, signature, sv, tempClassName, methodName)
    }
}

class WakeLockReplaceMethodVisitor(
    access: Int,
    descriptor: String?,
    signature: String?,
    val superVisitor: MethodVisitor,
    val className: String,
    val methodName: String?
) : AdviceAdapter(ASM9, superVisitor, access, methodName, descriptor) {

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        methodName: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        // println("visitMethodInsn $methodName")
        // Thread.sleep(50)
        if (opcode == INVOKEVIRTUAL
            && owner == "android/os/PowerManager\$WakeLock"
            && (methodName == "acquire" || methodName == "release")
            && descriptor != null
            && className != "me/xx2bab/bytecode/intro/WakeLockProxy"
        ) {
            // println("visitMethodInsn matched")
            val desc = StringBuilder().append(descriptor.substring(0, 1))
                .append("Landroid/os/PowerManager\$WakeLock;")
                .append(descriptor.substring(1, descriptor.length))
                .toString()
            superVisitor.visitMethodInsn(
                INVOKESTATIC,
                "me/xx2bab/bytecode/intro/WakeLockProxy",
                methodName,
                desc,
                false
            )
            return
        }
        super.visitMethodInsn(opcode, owner, methodName, descriptor, isInterface)
    }

}