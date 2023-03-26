package github.arusland.markplay.util

import org.codehaus.groovy.control.io.NullWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Writer

object ExecUtil {
    /**
     * Runs command and returns exit code
     */
    fun runCommand(
        command: List<String>,
        stdOutput: Writer = NullWriter.DEFAULT,
        errorOutput: Writer = NullWriter.DEFAULT
    ): Int {
        log.info("Running command: {}", command)

        val processBuilder = ProcessBuilder(command)
        val process = processBuilder.start()
        process.inputStream.bufferedReader().use { it.copyTo(stdOutput) }
        process.errorStream.bufferedReader().use { it.copyTo(errorOutput) }
        val exitCode = process.waitFor()

        log.info("Command '{}' exited with code {}", command, exitCode)

        return exitCode
    }

    private val log: Logger = LoggerFactory.getLogger(ExecUtil::class.java)!!
}
