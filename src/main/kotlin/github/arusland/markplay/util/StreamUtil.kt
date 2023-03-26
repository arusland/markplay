package github.arusland.markplay.util

import java.io.OutputStream
import java.io.Writer

fun OutputStream.writeFrom(writerSrc: Writer) = this.bufferedWriter().use { writer ->
    writer.write(writerSrc.toString())
}
