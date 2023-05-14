package me.xx2bab.extendagp.kspbasic

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile


class AggregatingTestProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return AggregatingTestProcessor(
            environment.codeGenerator,
            environment.logger
        )
    }
}

class AggregatingTestProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private val newActivities: MutableList<KSFile> = mutableListOf()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val newFiles = resolver.getNewFiles()
        newFiles.forEach {
            logger.info("process ${it.fileName}")
        }
        newActivities += newFiles.filter {
            it.fileName.contains("Activity")
        }
        return emptyList()
    }

    override fun finish() {
        super.finish()
        if (newActivities.isNotEmpty()) {
            val dependencies = Dependencies(aggregating = false,
                sources = newActivities.toTypedArray())
            val outputName = "outputForActivities"
            val output = codeGenerator.createNewFile(dependencies,
                "com.example", outputName, "kt")
            val content = "// ${newActivities.joinToString(", ") { it.fileName }}"
            output.write(content.toByteArray())
            output.close()
        }
    }

}