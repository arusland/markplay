package github.arusland.markplay.core

import github.arusland.markplay.util.ResourceUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class MarkplayTest {
    @Test
    fun testExec() {
        val markplay = Markplay()
        val samplePath = ResourceUtil.getResourcePath("/samples/SIMPLE1.md")
        val writer = StringWriter()
        markplay.exec(samplePath, writer)

        val expected = ResourceUtil.getResourceAsString("/samples/SIMPLE1.expected.md")

        assertEquals(expected, writer.toString())
    }
}
