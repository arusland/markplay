package github.arusland.markplay.groovy

import github.arusland.markplay.core.LanguageProvider
import groovy.lang.Binding
import groovy.lang.GroovyShell
import java.io.OutputStream
import java.io.PrintStream

/**
 * Implementation of [LanguageProvider] for Groovy
 */
class GroovyProvider : LanguageProvider {
    override fun execute(code: String, output: OutputStream): Any? {
        val binding = Binding()
        binding.setVariable("out", PrintStream(output))
        val groovyShell = GroovyShell(binding)
        val result = groovyShell.evaluate(code)
        output.flush()
        return result
    }
}
