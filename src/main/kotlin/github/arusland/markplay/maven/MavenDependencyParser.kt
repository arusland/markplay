package github.arusland.markplay.maven

class MavenDependencyParser {
    /**
     * Parse maven dependency from line comment in format:
     *  // dependency: org.apache.commons:commons-lang3:3.4
     */
    fun parseFromComment(input: String): List<MavenDependency> {
        return DEPENDENCY_PAT.findAll(input)
            .map { it.groupValues }
            .map { MavenDependency(it[1], it[2], it[3]) }
            .toList()
    }

    private companion object {
        private val DEPENDENCY_PAT = Regex("""\S*//\s*dependency:\s*(\S+):(\S+):(\S+)""")
    }
}

data class MavenDependency(val groupId: String, val artifactId: String, val version: String) {
    override fun toString(): String {
        return "$groupId:$artifactId:$version"
    }

    companion object {
        fun of(input: String): MavenDependency {
            val parts = input.split(":")
            return MavenDependency(parts[0], parts[1], parts[2])
        }
    }
}
