package sh.tnn.gradle.plugins.github

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import sh.tnn.gradle.util.useGitHubActionsMavenRepository
import sh.tnn.gradle.util.useGitHubMavenRepository

open class GitHub(
	private val project: Project,
	private val repos: RepositoryHandler,
) {
	fun use(owner: String, vararg repositories: String) {
		repositories.forEach { repo ->
			useGitHubMavenRepository(project, repos, owner, repo)
		}
		if (repositories.isEmpty()) useGitHubMavenRepository(project, repos, owner, "*")
	}

	fun actions() {
		useGitHubActionsMavenRepository(project, repos)
	}
}
