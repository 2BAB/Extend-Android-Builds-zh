package me.xx2bab.bytecode.intro

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*

class OnCreateClassVisitor(classWriter: ClassVisitor) : ClassVisitor(Opcodes.ASM9, classWriter) {

    private var currClassName = ""

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        println("visitClass $name")
        currClassName = name ?: ""
    }

    override fun visitMethod(
        access: Int,
        methodName: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        println("visitMethod: $methodName")
        val sv = super.visitMethod(access, methodName, descriptor, signature, exceptions)
        return if (currClassName.startsWith("me/xx2bab/activity")
            && methodName == "onCreate"
        ) {
            OnCreateMethodVisitor(currClassName, methodName, sv)
        } else {
            sv
        }
    }
}

class OnCreateMethodVisitor(
    private val currClassName: String,
    private val currMethodName: String,
    private val superVisitor: MethodVisitor,
) : MethodVisitor(ASM9, superVisitor) {

    override fun visitCode() {
        super.visitCode()
        superVisitor.visitLdcInsn("${currClassName}#${currMethodName}")
        superVisitor.visitMethodInsn(
            INVOKESTATIC,
            "me/xx2bab/bytecode/intro/Logger",
            "log",
            "(Ljava/lang/String;)V",
            false
        )
    }

}