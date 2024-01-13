package sh.tnn.gradle.plugins.java_environment

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.process.JavaForkOptions

class JavaEnvironmentPlugin : Plugin<Project> {
	companion object {
		private val tasksWithJavaForkOptions = mutableListOf<JavaForkOptions>()
	}

	override fun apply(target: Project) {
		target.tasks.all {
			if (it is JavaForkOptions) {
				tasksWithJavaForkOptions.add(it)
				it.environment = it.environment ?: mutableMapOf()
			}
		}
	}

	fun extend(environment: Map<String, String>) = tasksWithJavaForkOptions.forEach {
		it.environment.putAll(environment)
	}

	fun extend(vararg environment: Pair<String, String>) = extend(environment.toMap())

	fun set(key: String, value: String) = extend(key to value)

	fun remove(vararg keys: String) = tasksWithJavaForkOptions.forEach {
		keys.forEach { key -> it.environment.remove(key) }
	}

	fun append(key: String, value: String) = tasksWithJavaForkOptions.forEach {
		it.environment[key] = it.environment[key]?.let { previousValue -> "$previousValue$value" } ?: value
	}
}
