package github.arusland.markplay.java

import github.arusland.markplay.core.LanguageProvider
import github.arusland.markplay.util.ExecUtil
import github.arusland.markplay.util.StringUtil.NEWLINE
import github.arusland.markplay.util.writeFrom
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.OutputStream
import java.io.StringWriter
import java.lang.Thread.sleep
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JavaProvider : LanguageProvider {

    override fun execute(code: String, output: OutputStream): Any? {
        val tempDir = createTempDir()
        try {
            // write code to temp file
            val className = extractClassName(code)
            val javaFile = tempDir.resolve("$className.java")
            Files.write(javaFile, code.toByteArray())
            val javaBinDir = resoleJavaBin()
            val javacPath = javaBinDir.resolve("javac").toString()
            // compile temp file
            log.debug("Writing code to temp file and compile: {}", javaFile)
            val compileCmd = listOf(javacPath, javaFile.toString())
            val compileStdOutput = StringWriter()
            val compileErrorOutput = StringWriter()
            val complileExitCode = ExecUtil.runCommand(compileCmd, compileStdOutput, compileErrorOutput)
            if (complileExitCode != 0) {
                // TODO: handle error output in different way
                output.writeFrom(compileStdOutput)
                compileStdOutput.write("=====compilation failed with code: $complileExitCode =====")
                compileStdOutput.write(NEWLINE)
                output.write(removeTempPath(compileErrorOutput, tempDir))

                return null
            }
            log.debug("Running compiled class: {} in {}", className, tempDir)
            // run compiled class
            val javaPath = javaBinDir.resolve("java").toString()
            val javaClassPath = tempDir.toString()
            val runCmd = listOf(javaPath, "-cp", javaClassPath, className)
            val runOutput = StringWriter()
            val runErrorOutput = StringWriter()
            val runExitCode = ExecUtil.runCommand(runCmd, runOutput, runErrorOutput)
            output.writeFrom(runOutput)
            if (runExitCode != 0) {
                output.writeFrom(runErrorOutput)
            }

            return null
        } finally {
            log.debug("Deleting temp directory: {}", tempDir)
            tempDir.toFile().deleteRecursively()
        }
    }

    /**
     * Create temp directory with unique name
     */
    private fun createTempDir(): Path {
        val newDir = TEMP_DIR.resolve(LocalDateTime.now().format(DIR_TIME_FORMATTER))
        if (Files.exists(newDir)) {
            sleep(500)
            return createTempDir()
        }
        return Files.createDirectories(newDir).toAbsolutePath()
    }

    private fun extractClassName(code: String): String =
        CLASS_NAME_PAT.find(code)?.let { it.groupValues[1] }
            ?: throw IllegalArgumentException("Can't find class name in code")


    private fun resoleJavaBin(): Path {
        val javaHome = System.getProperty("java.home")
        val javaBin = Paths.get(javaHome, "bin")

        if (!Files.exists(javaBin)) {
            throw IllegalStateException("Can't find java bin directory: $javaBin")
        }

        return javaBin.toAbsolutePath()
    }

    /**
     * Remove temp path from error output
     */
    private fun removeTempPath(compileErrorOutput: StringWriter, tempDir: Path): ByteArray {
        val tempDirStr = tempDir.toString().let { if (it.endsWith("/")) it else "$it/" }
        val errorOutput = compileErrorOutput.toString()
        val result = errorOutput.replace(tempDirStr, "")

        return result.toByteArray()
    }

    private companion object {
        val log: Logger = LoggerFactory.getLogger(JavaProvider::class.java)!!
        val CLASS_NAME_PAT = Regex(" class\\s+(\\w+)\\s*")
        private val TEMP_DIR = Paths.get(System.getProperty("java.io.tmpdir"), "wrkpl")
        private val DIR_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    }
}
