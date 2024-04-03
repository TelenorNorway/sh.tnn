package sh.tnn.gradle.util

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import sh.tnn.gradle.plugins.dotenv.DotEnvPlugin
import java.net.URI

private data class GithubCredentials(
	val username: String,
	val password: String,
)

private object Credentials {
	private var cache: GithubCredentials? = null

	private fun getGitHubCredentials(project: Project) = GithubCredentials(
		DotEnvPlugin.get(project, "GPR_ACTOR") ?: DotEnvPlugin.get(project, "GITHUB_ACTOR") ?: System.getProperty("gpr.actor")
		?: throw Throwable("GPR_ACTOR environment variable is not set"),
		DotEnvPlugin.get(project, "GPR_TOKEN") ?: DotEnvPlugin.get(project, "GITHUB_TOKEN") ?: System.getProperty("gpr.token")
		?: throw Throwable("GPR_TOKEN environment variable is not set")
	)

	fun get(project: Project): GithubCredentials {
		if (cache == null) cache = getGitHubCredentials(project)
		return cache!!
	}
}

private val INVALID_CHARS_REGEX = Regex("[^A-Za-z0-9_.]+")
private fun String.toValidMavenName() = replace(INVALID_CHARS_REGEX, "-")

@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
fun useGitHubMavenRepository(project: Project, repo: RepositoryHandler, owner: String, repository: String) = repo.maven { r ->
	r.name = "GitHubPackages $owner/$repository".toValidMavenName()
	r.url = URI.create("https://maven.pkg.github.com/$owner/$repository")
	r.credentials {
		val gh = Credentials.get(project)
		it.username = gh.username
		it.password = gh.password
	}
}!!

fun useGitHubActionsMavenRepository(project: Project, repo: RepositoryHandler): MavenArtifactRepository? {
	if ((System.getenv("GITHUB_ACTIONS") ?: "false") != "true") return null
	val (owner, repository) = System.getenv("GITHUB_REPOSITORY")?.split("/")
		?: throw Exception("\$GITHUB_REPOSITORY is missing from the environment variables! In CI/CD set this to \${{ github.repository }}")
	return useGitHubMavenRepository(project, repo, owner, repository)
}
