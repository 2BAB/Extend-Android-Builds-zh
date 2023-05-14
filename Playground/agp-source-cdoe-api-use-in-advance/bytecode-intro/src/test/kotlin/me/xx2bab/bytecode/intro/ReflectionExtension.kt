package me.xx2bab.bytecode.intro

import android.os.Bundle

fun Any.invokeMethod(clazz: Class<*>, methodName: String, paramTypes: List<Class<*>>, vararg args: Any) {
    val method = this::class.java.getDeclaredMethod(methodName, *paramTypes.toTypedArray())
    method.isAccessible = true
    if (args.isEmpty()) {
        method.invoke(this)
    } else {
        method.invoke(this, *(args ?: emptyArray()))
    }
}

fun Any.invokeMethodWithReturnValue(clazz: Class<*>, methodName: String, vararg args: String): String {
    val method = this::class.java.getDeclaredMethod(methodName)
    method.isAccessible = true
    return if (args.isEmpty()) {
        method.invoke(this).toString()
    } else {
        method.invoke(this, args).toString()
    }
}

fun Any.getFieldValueInString(clazz: Class<*>, fieldName: String): String {
    val field = clazz.getDeclaredField(fieldName)
    field.isAccessible = true
    return field.get(this).toString()
}

fun Class<*>.printAllFields() {
    fields.forEach {
        println("[Caliper][Debug][printAllFields]" + it.name)
    }
}

fun Class<*>.printAllMethods() {
    methods.forEach {
        println("[Caliper][Debug][printAllMethods]" + it.name)
    }
}