package github.arusland.markplay.core

import github.arusland.markplay.parser.Code
import github.arusland.markplay.parser.MarkdownParser
import github.arusland.markplay.parser.PartType
import github.arusland.markplay.parser.Text
import github.arusland.markplay.render.MarkdownRender
import github.arusland.markplay.util.StringUtil.NEWLINE
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.Writer
import java.nio.file.Path
import kotlin.io.path.inputStream

/**
 * Execute code blocks from markdown file and write as markdown
 */
class Markplay {
    /**
     * Execute code blocks from markdown and write resulting markdown to [output]
     */
    fun exec(text: String, output: Writer) {
        exec(text.byteInputStream(), output)
    }

    /**
     * Execute code blocks from markdown file and write resulting markdown to [output]
     */
    fun exec(input: Path, output: Writer) {
        input.inputStream().use { stream ->
            exec(stream, output)
        }
    }

    /**
     * Execute code blocks from [InputStream] and write resulting markdown to [output]
     */
    fun exec(inputStream: InputStream, output: Writer) {
        val parser = MarkdownParser()
        val parts = parser.parse(inputStream).toMutableList()
        val codeBlocks = parts.filter { it.type == PartType.Code }.map { it as Code }

        codeBlocks.forEach { codeBlock ->
            val index = parts.indexOf(codeBlock)
            val provider = SupportedLanguages.getProvider(codeBlock.language)

            if (provider != null) {
                val stdOutput = ByteArrayOutputStream()
                provider.execute(codeBlock.code, stdOutput)
                val textOutput = String(stdOutput.toByteArray(), Charsets.UTF_8)

                parts.add(index + 1, Text("${NEWLINE}Output:$NEWLINE"))
                parts.add(index + 2, Code("```", textOutput, "```"))
            }
            // TODO: Add support for binary output
            // TODO: Add support for other java
            // TODO: Add support for other bash
            // TODO: Find new files after execution and try to render them (images, pdf, etc.)
            // TODO: Detect image links and render them as image
            // TODO: Detect youtube links and render them as video
        }

        val render = MarkdownRender()
        render.write(parts, output)
    }
}
