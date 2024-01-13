package sh.tnn.gradle.plugins.dotenv

import io.github.cdimascio.dotenv.dotenv
import org.gradle.api.Plugin
import org.gradle.api.Project
import sh.tnn.gradle.plugins.java_environment.JavaEnvironmentPlugin

/**
 * A feature that automatically loads environment variables
 * from a `.env` file.
 */
class DotEnvPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		project.plugins.withType(JavaEnvironmentPlugin::class.java) {
			it.extend(rootProjectDotenv(project))
		}
	}

	companion object {
		private var env: Map<String, String>? = null

		private fun createEnvironment(project: Project): Map<String, String> {
			val e = mutableMapOf<String, String>()
			env = e
			e.putAll(dotenv {
				directory = project.rootProject.projectDir.absolutePath
				ignoreIfMissing = true
				ignoreIfMalformed = true
			}.entries().associate { it.key to it.value })
			return e
		}

		fun rootProjectDotenv(project: Project) = env ?: createEnvironment(project.rootProject)

		fun get(project: Project, key: String): String? = rootProjectDotenv(project)[key] ?: System.getenv(key)
	}
}
