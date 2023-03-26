package github.arusland.markplay.util

import java.nio.file.Path
import kotlin.io.path.toPath

object ResourceUtil {
    fun getResourcePath(name: String): Path = javaClass.getResource(name)?.toURI()?.toPath()
        ?: throw Exception("Resource not found: $name")

    fun getResourceAsString(name: String): String = getResourcePath(name).toFile().readText()
}
