package github.arusland.markplay.parser

class MarkdownParser {
    fun parse(markdown: String): String {
        TODO()
    }
}

data class Part(val type: PartType, val text: String)

enum class PartType {
    Text,

    Code
}
