package github.arusland.markplay.core

import github.arusland.markplay.groovy.GroovyProvider
import github.arusland.markplay.java.JavaProvider
import java.io.OutputStream

interface LanguageProvider {
    fun execute(code: String, output: OutputStream): Any?
}

object SupportedLanguages {
    private val groovyProvider = GroovyProvider()
    private val javaProvider = JavaProvider()

    fun getProvider(name: String): LanguageProvider? =
        when (name) {
            "groovy" -> groovyProvider
            "java" -> javaProvider
            else -> null
        }
}
