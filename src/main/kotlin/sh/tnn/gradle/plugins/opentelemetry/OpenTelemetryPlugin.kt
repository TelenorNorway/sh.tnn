package sh.tnn.gradle.plugins.opentelemetry

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logging
import sh.tnn.gradle.plugins.dotenv.DotEnvPlugin
import sh.tnn.gradle.plugins.java_environment.JavaEnvironmentPlugin

class OpenTelemetryPlugin : Plugin<Project> {
	private val logger = Logging.getLogger(OpenTelemetryPlugin::class.java)

	override fun apply(project: Project) {

		val openTelemetryJavaAgentPath = DotEnvPlugin.get(project, "OTEL_JAVA_AGENT")

		if (openTelemetryJavaAgentPath == null) {
			logger.debug("OpenTelemetry Java Agent path is not set. Not applying OpenTelemetry plugin.")
			return
		}

		val openTelemetryJavaAgentFile = project.file(openTelemetryJavaAgentPath)

		if (!openTelemetryJavaAgentFile.exists()) {
			logger.warn("OpenTelemetry Java Agent file does not exist. Not applying OpenTelemetry plugin. Given path: $openTelemetryJavaAgentPath")
			return
		}

		logger.debug("Applying OpenTelemetry plugin. With Java Agent: ${openTelemetryJavaAgentFile.absolutePath}")

		project.plugins.withType(JavaEnvironmentPlugin::class.java) {
			val resourceName = project.name
			val replicaSetName = "${resourceName}-0000000000"
			val podName = "${replicaSetName}-00000"
			val nodeName = "local-spot-00000000-host000000"
			it.extend(
				"OTEL_RESOURCE_ATTRIBUTES_POD_NAME" to podName,
				"OTEL_METRICS_EXPORTER" to (DotEnvPlugin.get(project, "OTEL_METRICS_EXPORTER") ?: "otlp"),
				"OTEL_RESOURCE_ATTRIBUTES" to mapOf(
					"k8s.container.name" to resourceName,
					"k8s.deployment.name" to resourceName,
					"k8s.namespace.name" to "t-s00000-local",
					"k8s.node-name" to nodeName,
					"k8s.pod.name" to podName,
					"k8s.replicaset.name" to replicaSetName,
					"service.version" to "UNVERSIONED",
				).map { e -> "${e.key}=${e.value}" }.joinToString(","),
				"OTEL_PROPAGATORS" to "tracecontext,baggage",
				"OTEL_EXPORTER_OTLP_ENDPOINT" to (DotEnvPlugin.get(project, "OTEL_EXPORTER_OTLP_ENDPOINT")
					?: "http://localhost:4317"),
				"OTEL_TRACES_SAMPLER" to "always_on",
				"OTEL_SERVICE_NAME" to resourceName,
				"OTEL_RESOURCE_ATTRIBUTES_NODE_NAME" to nodeName
			)
			it.append("JAVA_TOOL_OPTIONS", " -javaagent:${openTelemetryJavaAgentFile.absolutePath}")
		}
	}
}
