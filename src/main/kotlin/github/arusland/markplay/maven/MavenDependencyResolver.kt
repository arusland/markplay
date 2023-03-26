package github.arusland.markplay.maven

import com.tobedevoured.naether.api.Naether
import com.tobedevoured.naether.impl.NaetherImpl
import org.slf4j.LoggerFactory
import org.sonatype.aether.repository.RemoteRepository
import java.io.File
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

/**
 * Resolves maven dependencies
 */
class MavenDependencyResolver(val repoDir: Path = getStdRepoDir(), val repoRemoteUrl: URL? = null) {

    /**
     * Resolves dependencies and returns list of URLs to jars
     *
     * TODO: try another library for resolving dependencies
     */
    fun resolveDependencies(dependencies: List<MavenDependency>): List<URL> {
        // TODO: check dependency order
        val jars = LinkedHashSet<URL>()

        for (dependency in dependencies) {
            jars.addAll(resolveDependencies(dependency))
        }

        return jars.toList()
    }

    fun resolveDependencies(dependency: MavenDependency): List<URL> {
        log.debug("Resolving '{}' dependencies...", dependency)
        val lastTime = System.currentTimeMillis()
        val naether: Naether = NaetherImpl()
        naether.localRepoPath = repoDir.absolutePathString()
        if (repoRemoteUrl != null) {
            naether.remoteRepositories =
                setOf(RemoteRepository(null, null, repoRemoteUrl.toString()))
        }
        naether.addDependency(dependency.toString())
        naether.resolveDependencies()
        val elapsedTime = System.currentTimeMillis() - lastTime
        for (dep in naether.dependenciesNotation) {
            log.debug("Resolved dependency for dependency '{}': {}", dependency, dep)
        }
        val jars = naether.resolvedClassPath.split(DELIMITER)
            .map(this::toUrl)
        log.debug("Dependencies ({}) resolved in {} ms", jars.size, elapsedTime)
        return jars
    }

    private fun toUrl(url: String): URL = File(url).toURI().toURL()

    private companion object {
        val log = LoggerFactory.getLogger(MavenDependencyResolver::class.java)!!
        val DELIMITER = ":".toRegex()

        fun getStdRepoDir(): Path {
            val home = System.getProperty("user.home")
            return Paths.get(home, ".m2", "repository")
        }
    }
}
