package me.xx2bab.extendagp.kspbasic

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo
import com.squareup.kotlinpoet.ksp.toTypeName

class SampleProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return SampleProcessor(
            environment.codeGenerator,
            environment.logger
        )
    }
}

class SampleProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private val dataClipAnnotation = "me.xx2bab.extendagp.kspbasic.DataClip"

    @OptIn(KotlinPoetKspPreview::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val targets = resolver.getSymbolsWithAnnotation(dataClipAnnotation)
        val ret = targets.filter { !it.validate() }.toList()
        targets.filter { it.validate() }

        targets.filter {
            it.validate()
                    && it is KSClassDeclaration
                    && it.classKind == ClassKind.CLASS
        }.forEach { ksAnnotated ->
            val classDeclaration = ksAnnotated as KSClassDeclaration
            val packageName = classDeclaration.packageName.asString()
            val dataClipModel = parseDataClipArguments(classDeclaration)

            val fileSpec = FileSpec.builder(packageName, dataClipModel.className)
            val classBuilder =
                TypeSpec.classBuilder(dataClipModel.className).addModifiers(KModifier.DATA)
            val primaryConstructor = FunSpec.constructorBuilder()

            classDeclaration.declarations.forEach { declaration ->
                if (declaration is KSPropertyDeclaration
                    && dataClipModel.isRequired(declaration.simpleName.asString())
                ) {
                    logger.info("prop: " + declaration.qualifiedName?.asString())
                    logger.info("prop: " + declaration.type.toString())
                    primaryConstructor.addParameter(
                        ParameterSpec(
                            declaration.simpleName.asString(),
                            declaration.type.toTypeName(),
                        )
                    )

                    val prop = PropertySpec.builder(
                        declaration.simpleName.asString(),
                        declaration.type.toTypeName()
                    )
                        .initializer(declaration.simpleName.asString())
                        .build()
                    classBuilder.addProperty(prop)
                }
            }
            val targetClass = classBuilder.primaryConstructor(primaryConstructor.build()).build()
            fileSpec.addType(targetClass)
            fileSpec.build().writeTo(
                codeGenerator,
                Dependencies(false, ksAnnotated.containingFile!!)
            )
        }
        return ret
    }

    private fun parseDataClipArguments(classDeclaration: KSClassDeclaration): DataClipModel {
        val dataClipModel = DataClipModel()
        classDeclaration.annotations
            .first { it.shortName.asString() == "DataClip" }
            .arguments.forEach {
                when (it.name!!.asString()) {
                    "className" -> {
                        dataClipModel.className = it.value.toString()
                    }
                    "include" -> {
                        dataClipModel.includes.addAll(it.value as ArrayList<String>)
                    }
                    "exclude" -> {
                        dataClipModel.includes.addAll(it.value as ArrayList<String>)
                    }
                }
            }
        dataClipModel.mode = if (dataClipModel.includes.isNotEmpty()) {
            ClipMode.INCLUDE
        } else {
            ClipMode.EXCLUDE
        }
        return dataClipModel
    }

}


