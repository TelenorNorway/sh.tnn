package sh.tnn.gradle.plugins.telenor

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import sh.tnn.gradle.plugins.github.GitHub
import sh.tnn.gradle.plugins.github.GitHubPlugin

/**
 * A feature that lets the user easily add Telenor repositories
 * as maven repositories.
 */
class TelenorPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		if (project.rootProject == project) {
			rootProject(project)
		} else {
			childProject(project)
		}
	}

	private fun rootProject(project: Project) {
		childProject(project)
	}

	private fun childProject(project: Project) {
		project.plugins.withType(GitHubPlugin::class.java) {
			val gh = (project.repositories as ExtensionAware).extensions.getByType(GitHub::class.java)
				?: throw Throwable("GitHub plugin not applied")

			(project.repositories as ExtensionAware).extensions.create(
				"telenor",
				TelenorRepositories::class.java,
				project.repositories,
				gh
			)
		}
	}
}
