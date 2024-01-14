package sh.tnn.gradle.plugins.auto_exclude

import org.gradle.api.Plugin
import org.gradle.api.Project
import sh.tnn.gradle.plugins.dotenv.DotEnvPlugin

class AutoExcludePlugin : Plugin<Project> {
	companion object {
		val excluded = arrayOf(
			"commons-logging:commons-logging" unlessKeep "commons-logging",
		)
	}

	override fun apply(project: Project) {
		val keep = (DotEnvPlugin.get(project, "AUTO_EXCLUDE_KEEP") ?: "").split(",").map { it.trim().lowercase() }
		project.configurations.all { conf ->
			excluded.forEach { notation ->
				if (notation.second in keep) return@forEach
				val (group, module) = notation.first.split(":")
				conf.exclude(mapOf("group" to group, "module" to module))
			}
		}
	}
}

private infix fun String.unlessKeep(other: String): Pair<String, String> = this to other.lowercase()
