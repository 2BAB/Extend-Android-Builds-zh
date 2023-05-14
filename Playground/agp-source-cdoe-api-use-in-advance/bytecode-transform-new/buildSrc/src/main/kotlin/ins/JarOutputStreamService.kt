package ins

import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingQueue
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

data class TransformedClass(
    val relativePath: String,
    val byteArray: ByteArray
)

abstract class JarOutputStreamService : BuildService<BuildServiceParameters.None> {

    private val processedBytecodeQueue = LinkedBlockingQueue<TransformedClass>()

    fun offer(item: TransformedClass) {
        processedBytecodeQueue.offer(item)
    }

    // I guess an App should not have a number of classes
    // that is greater than 2^31 (Integer.MAX_VALUE)
    fun startWithJobCountLimit(outputJar: File, jobCount: Int) {
        val jarOutput = JarOutputStream(BufferedOutputStream(FileOutputStream(outputJar)))
        val jobCountDownLatch = CountDownLatch(jobCount)
        while (jobCountDownLatch.count > 0) {
            val item = processedBytecodeQueue.take()
            jobCountDownLatch.countDown()
            jarOutput.putNextEntry(JarEntry(item.relativePath))
            jarOutput.write(item.byteArray)
            jarOutput.closeEntry()
            // println("JarOutputStreamService: ${jobCountDownLatch.count}")
        }
        jarOutput.close()
    }

}