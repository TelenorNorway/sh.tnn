package sh.tnn.gradle.plugins.github

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.plugins.PublishingPlugin

/**
 * A feature that lets the user easily add GitHub Packages
 * as maven repositories.
 */
class GitHubPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		installOn(project, project.repositories)
		project.plugins.withType(PublishingPlugin::class.java) {
			installOn(project, project.extensions.getByType(PublishingExtension::class.java).repositories)
		}
	}

	private fun installOn(project: Project, repos: RepositoryHandler) {
		(repos as ExtensionAware).extensions.create("github", GitHub::class.java, project, repos)
	}
}
