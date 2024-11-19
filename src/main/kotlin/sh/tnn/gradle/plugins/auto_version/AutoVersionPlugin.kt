package sh.tnn.gradle.plugins.auto_version

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A feature that automatically sets the project version
 * based on the `VERSION` environment variable.
 */
class AutoVersionPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		if (project.version != "unspecified") {
			return
		}
		project.version = System.getenv("VERSION") ?: "UNVERSIONED"
	}
}
