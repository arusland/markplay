package github.arusland.markplay.render

import github.arusland.markplay.parser.MarkdownParser
import github.arusland.markplay.parser.Part
import java.io.Writer

/**
 * Render markdown parts
 * @see MarkdownParser
 */
class MarkdownRender {
    fun write(parts: List<Part>, writer: Writer) {
        parts.forEach { part ->
            writer.write(part.asString())
        }
    }
}
