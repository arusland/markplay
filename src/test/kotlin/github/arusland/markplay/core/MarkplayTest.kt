package github.arusland.markplay.core

import github.arusland.markplay.util.ResourceUtil
import github.arusland.markplay.util.StringUtil.NEWLINE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class MarkplayTest {
    @Test
    fun testExecEmptyString() {
        val markplay = Markplay()
        val writer = StringWriter()
        markplay.exec("", writer)

        assertEquals("", writer.toString())
    }

    @Test
    fun testExecNewlinesString() {
        val markplay = Markplay()
        val writer = StringWriter()
        val markdown = " $NEWLINE $NEWLINE $NEWLINE"
        markplay.exec(markdown, writer)

        assertEquals(markdown, writer.toString())
    }

    @Test
    fun testExec() {
        val markplay = Markplay()
        val samplePath = ResourceUtil.getResourcePath("/samples/SIMPLE1.md")
        val writer = StringWriter()
        markplay.exec(samplePath, writer)

        val expected = ResourceUtil.getResourceAsString("/samples/SIMPLE1.expected.md")

        assertEquals(expected, writer.toString())
    }

    @Test
    fun testExecWhenCodeBlockNotClosed() {
        val markplay = Markplay()
        val markdown = ResourceUtil.getResourceAsString("/samples/UnclosedCodeBlock.md")
        val writer = StringWriter()
        markplay.exec(markdown, writer)

        assertEquals(markdown, writer.toString())
    }
}
