package github.arusland.markplay.core

import github.arusland.markplay.groovy.GroovyProvider
import java.io.OutputStream

interface LanguageProvider {
    fun execute(code: String, output: OutputStream): Any?
}

object SupportedLanguages {
    private val groovyProvider = GroovyProvider()

    fun getProvider(name: String): LanguageProvider? =
        when (name) {
            "groovy" -> groovyProvider
            else -> null
        }
}
