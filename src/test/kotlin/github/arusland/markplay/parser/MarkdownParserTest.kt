package github.arusland.markplay.parser

import java.io.FileWriter

class MarkdownParserTest {
    @org.junit.jupiter.api.Test
    fun parse() {
        val parser = MarkdownParser()
        val markdown = javaClass.getResourceAsStream("/samples/SIMPLE1.md")
            .use { it?.reader()?.readText() } ?: throw Exception("Can't read resource file.")
        val output = parser.parse(markdown)

        FileWriter("out.md").use { writer ->
            writer.write(output)
        }
    }
}
