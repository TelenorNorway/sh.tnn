plugins {
	kotlin("jvm") version "1.9.21"
	id("com.gradle.plugin-publish") version "1.2.1"
	`java-gradle-plugin`
}

group = "no.gpkg"
version = System.getenv("VERSION") ?: "UNVERSIONED"
repositories.mavenCentral()
kotlin.jvmToolchain(17)

repositories {
	gradlePluginPortal()
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
		}
	}
}
