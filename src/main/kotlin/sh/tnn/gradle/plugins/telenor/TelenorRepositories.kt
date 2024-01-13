package sh.tnn.gradle.plugins.telenor

import org.gradle.api.artifacts.dsl.RepositoryHandler
import sh.tnn.gradle.plugins.github.GitHub
import java.net.URI

open class TelenorRepositories(
	private val repos: RepositoryHandler,
	private val github: GitHub
) {
	private fun repos(owner: String, repositories: Array<out String>) {
		repositories.forEach {
			github.use(owner, it)
		}
		if (repositories.isEmpty()) {
			github.use(owner)
		}
	}

	fun internal(vararg repositories: String) = repos("telenornorgeinternal", repositories)
	fun public(vararg repositories: String) = repos("telenornorway", repositories)

	fun github() {
		internal()
		public()
	}

	fun dcapi() {
		repos.maven { r -> r.url = URI.create("https://prima.corp.telenor.no/nexus/repository/dcapi") }
	}

	fun legacyMirror() {
		repos.maven { r -> r.url = URI.create("https://prima.corp.telenor.no/nexus/repository/public") }
	}
}
