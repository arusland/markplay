package github.arusland.markplay.maven

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class MavenDependencyResolverTest {
    @Disabled
    @Test
    fun testResolveDependenciesInternal() {
        val resolver = MavenDependencyResolver()
        val jars = resolver.resolveDependencies(MavenDependency.of("org.apache.commons:commons-lang3:3.4"))

        jars.forEach { dep ->
            println(dep)
        }
    }
}
