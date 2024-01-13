package sh.tnn.gradle.util

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import java.net.URI

private data class GithubCredentials(
	val username: String,
	val password: String,
)

private object Credentials {
	private var cache: GithubCredentials? = null

	private fun getGitHubCredentials() = GithubCredentials(
		System.getenv("GPR_ACTOR") ?: System.getenv("GITHUB_ACTOR")
		?: throw Throwable("GPR_ACTOR environment variable is not set"),
		System.getenv("GPR_TOKEN") ?: System.getenv("GITHUB_TOKEN")
		?: throw Throwable("GPR_TOKEN environment variable is not set")
	)

	fun get(): GithubCredentials {
		if (cache == null) cache = getGitHubCredentials()
		return cache!!
	}
}

private val INVALID_CHARS_REGEX = Regex("[^A-Za-z0-9_.]+")
private fun String.toValidMavenName() = replace(INVALID_CHARS_REGEX, "-")

@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
fun useGitHubMavenRepository(repo: RepositoryHandler, owner: String, repository: String) = repo.maven { r ->
	r.name = "GitHubPackages $owner/$repository".toValidMavenName()
	r.url = URI.create("https://maven.pkg.github.com/$owner/$repository")
	r.credentials {
		val gh = Credentials.get()
		it.username = gh.username
		it.password = gh.password
	}
}!!

fun useGitHubActionsMavenRepository(repo: RepositoryHandler): MavenArtifactRepository? {
	if ((System.getenv("GITHUB_ACTIONS") ?: "false") != "true") return null
	val (owner, repository) = System.getenv("GITHUB_REPOSITORY")?.split("/")
		?: throw Exception("\$GITHUB_REPOSITORY is missing from the environment variables! In CI/CD set this to \${{ github.repository }}")
	return useGitHubMavenRepository(repo, owner, repository)
}
