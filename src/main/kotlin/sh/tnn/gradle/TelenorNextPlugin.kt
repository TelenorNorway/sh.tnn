package sh.tnn.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import sh.tnn.gradle.plugins.auto_exclude.AutoExcludePlugin
import sh.tnn.gradle.plugins.auto_version.AutoVersionPlugin
import sh.tnn.gradle.plugins.dotenv.DotEnvPlugin
import sh.tnn.gradle.plugins.github.GitHubPlugin
import sh.tnn.gradle.plugins.java_environment.JavaEnvironmentPlugin
import sh.tnn.gradle.plugins.opentelemetry.OpenTelemetryPlugin
import sh.tnn.gradle.plugins.telenor.TelenorPlugin

class TelenorNextPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		arrayOf(
			AutoVersionPlugin::class,
			JavaEnvironmentPlugin::class,
			DotEnvPlugin::class,
			GitHubPlugin::class,
			TelenorPlugin::class,
			OpenTelemetryPlugin::class,
			AutoExcludePlugin::class,
		).forEach {
			project.pluginManager.apply(it.java)
		}
	}
}
