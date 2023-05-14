package me.xx2bab.extendagp.buildlogic

import me.xx2bab.gradle.lifecycle.build.Lifecycle
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.events.OperationCompletionListener
import org.gradle.tooling.events.FailureResult
import org.gradle.tooling.events.FinishEvent
import org.gradle.tooling.events.OperationResult
import org.gradle.tooling.events.SkippedResult
import org.gradle.tooling.events.SuccessResult
import org.gradle.tooling.events.task.TaskFinishEvent
import org.gradle.tooling.events.task.TaskOperationDescriptor
import org.gradle.tooling.events.task.TaskProgressEvent
import org.gradle.tooling.events.task.TaskStartEvent

abstract class BuildFinishTrackService : BuildService<BuildFinishTrackService.Parameters>,
    OperationCompletionListener, AutoCloseable {

    interface Parameters : BuildServiceParameters {
    }

//    private val eventProcessorMap = mapOf(
//        ProjectConfigurationProgressEvent::class to ::processWorkItem,
//        FileDownloadProgressEvent::class to ::processWorkItem,
//        TaskProgressEvent::class to ::processWorkItem,
//        TestProgressEvent::class to ::processWorkItem,
//        TransformProgressEvent::class to ::processWorkItem,
//        WorkItemProgressEvent::class to ::processWorkItem,
//    )

    private fun OperationResult.isSuccess() = this is SuccessResult

    override fun onFinish(event: FinishEvent) {
//        eventProcessorMap[event::class]?.invoke()
        if (event is TaskProgressEvent) {
            when (event) {
                is TaskStartEvent -> {
                    Lifecycle.afterTask("TaskStartEvent")
                }

                is TaskFinishEvent -> {
                    Lifecycle.afterTask("TaskFinishEvent")
                    when (val result = event.result) {
                        is SuccessResult -> {
                            Lifecycle.afterTask(
                                "${event::class.simpleName} >>> ${result::class.simpleName} "
                                        + ">>> Result: Success. >>> EndTime: ${event.result.endTime}."
                            )
                            val desc: TaskOperationDescriptor = event.descriptor
                            Lifecycle.afterTask("taskPath: ${desc.taskPath}")
//                            Lifecycle.afterTask("taskPath: ${desc.dependencies.first().displayName}")
                            // Lifecycle.afterTask("${desc.taskPath} belongs to ${desc.originPlugin ?: "Unknown"}")
                        }

                        is SkippedResult -> {
                            Lifecycle.afterTask(
                                "${event::class.simpleName} >>> ${result::class.simpleName} "
                                        + ">>> Result: Skipped. >>> EndTime: ${event.result.endTime}."
                            )
                        }

                        is FailureResult -> {
                            Lifecycle.afterTask(
                                "${result::class.qualifiedName} >>> Result: Failed "
                                        + ">>> EndTime: ${event.result.endTime}.\r\n"
                                        + result.failures.joinToString(";")
                            )
                        }
                    }
                }
            }

        }
    }

    override fun close() {
        Lifecycle.onBuildFinished("Trigger by AutoCloseable.")
    }

}
