package github.arusland.markplay.parser

import github.arusland.markplay.util.StringUtil.NEWLINE
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.inputStream

/**
 * Parse markdown file to [Part]s
 */
class MarkdownParser {
    fun parse(text: String): List<Part> {
        return parse(ByteArrayInputStream(text.toByteArray()))
    }

    fun parse(path: Path): List<Part> {
        path.inputStream().use { stream ->
            return parse(stream)
        }
    }

    fun parse(path: InputStream): List<Part> {
        var codeBlockStart: String? = null
        val result = mutableListOf<Part>()
        val sb = StringBuilder()
        path.bufferedReader().use { reader ->
            reader.lines().forEach { line ->
                if (line.startsWith("```")) {
                    if (codeBlockStart != null) {
                        result.add(Code(codeBlockStart!!, sb.toString(), line))
                        sb.clear()
                        codeBlockStart = null
                    } else {
                        if (sb.isNotEmpty()) {
                            result.add(Text(sb.toString()))
                            sb.clear()
                        }

                        codeBlockStart = line
                    }
                } else {
                    sb.appendLine(line)
                }
            }
        }

        if (codeBlockStart != null) {
            result.add(Code(codeBlockStart!!, sb.toString(), "```"))
        } else if (sb.isNotEmpty()) {
            result.add(Text(sb.toString()))
        }

        return result
    }

    private fun StringBuilder.appendLine(line: String) {
        this.append(line)
        this.append(NEWLINE)
    }
}

abstract class Part(val type: PartType, val text: String) {
    open fun asString(): String = text
}

open class Text(text: String) : Part(PartType.Text, text)

open class Code(val begin: String, code: String, val end: String) : Part(PartType.Code, code) {
    val code: String = super.text

    val language: String = if (begin.length > 3) begin.substring(3).trimEnd() else ""

    override fun asString(): String = "$begin$NEWLINE$text$end$NEWLINE"
}

enum class PartType {
    Text,

    Code
}
