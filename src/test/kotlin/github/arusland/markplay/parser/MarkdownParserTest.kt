package github.arusland.markplay.parser

import github.arusland.markplay.render.MarkdownRender
import github.arusland.markplay.util.ResourceUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringWriter

class MarkdownParserTest {
    @Test
    fun parseAndRender() {
        val parser = MarkdownParser()
        val markdown = ResourceUtil.getResourceAsString("/samples/SIMPLE1.md")
        val parts = parser.parse(markdown)

        assertEquals(listOf(PartType.Text, PartType.Code, PartType.Text), parts.map { it.type })

        // rendered parts should be equal to original markdown
        val render = MarkdownRender()
        StringWriter().use { writer ->
            render.write(parts, writer)

            assertEquals(markdown, writer.toString())
        }
    }
}
