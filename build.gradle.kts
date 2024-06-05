plugins {
	kotlin("jvm") version "1.9.21"
	id("com.gradle.plugin-publish") version "1.2.1"
	`java-gradle-plugin`
	`maven-publish`
}

group = "sh.tnn"
version = System.getenv("VERSION") ?: "UNVERSIONED"
repositories.mavenCentral()
kotlin.jvmToolchain(17)

repositories {
	mavenCentral()
}

dependencies {
	implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
}

gradlePlugin {
	website = "https://github.com/telenornorway/sh.tnn"
	vcsUrl = "https://github.com/telenornorway/sh.tnn"
	plugins {
		create("gradle") {
			id = "sh.tnn"
			displayName = "Telenor Next"
			description = "Telenor Next Gradle Plugin"
			implementationClass = "sh.tnn.gradle.TelenorNextPlugin"
			tags = listOf("telenor", "next", "tnn", "dotenv", "github", "github-packages", "opentelemetry", "otel", "auto-instrumentation")
		}
	}
}

publishing {
	repositories {
		if (System.getenv("GITHUB_ACTIONS") == "true") {
			maven {
				name = "GitHubPackages"
				url = uri("https://maven.pkg.github.com/${System.getenv("GITHUB_REPOSITORY")}")
				credentials {
					username = System.getenv("GITHUB_ACTOR") ?: System.getenv("GPR_ACTOR") ?: System.getProperty("gpr.actor")
					password = System.getenv("GITHUB_TOKEN") ?: System.getenv("GPR_TOKEN") ?: System.getProperty("gpr.token")
				}
			}
		}
	}
}
