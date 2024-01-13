# Telenor Next Gradle Plugin

This plugin provides a set of features that are convenient
for developing projects at Telenor. Anyone can use this
plugin. It is not limited to Telenor employees.

## Usage

### Kotlin DSL

<!-- @formatter:off -->
```kts
plugins {
  id("sh.tnn") version "0.1.0"
}
```
<!-- @formatter:on -->

### Groovy DSL

<!-- @formatter:off -->
```groovy
plugins {
  id 'sh.tnn' version '0.1.0'
}
```
<!-- @formatter:on -->

## Prerequisites

You must add your GitHub username and personal access token
to your environment variables. You must generate a classic
personal access token with the `read:packages` scope.

```bash
export GPR_ACTOR="octocat"
export GPR_TOKEN="<insert token here>"
```

## Features

- [Auto versioning](#auto-versioning)
- [DotEnv](#dotenv)
- [OpenTelemetry](#opentelemetry)
- [GitHub Repositories](#github-repositories)
  - [Maven Publish integration](#maven-publish-integration)
- [Telenor Repositories](#telenor-repositories)

### Auto versioning

The plugin will automatically set the version of the project
using the `VERSION` environment variable. This variable will
default to `"UNVERSIONED"` when the variable is not set.
This is useful when setting the version of a project in
CI/CD.

```diff
  group = "org.example"
- version = "1.2.3"
```

### DotEnv

The plugin will automatically detect the presence of a
`.env` file in the project root. If the file is present, it
will be parsed and the variables will be added to any task
that implements `JavaForkOptions`.

### OpenTelemetry

The plugin will automatically add the OpenTelemetry Java
agent to any task that implements `JavaForkOptions`. The
agent will be configured to send traces and metrics to
the OpenTelemetry Collector running on the host machine.

To opt-in on this feature, download the Open Telemetry
Java agent somewhere in your home directory. In your
shell configurations (or in Windows environment
configurations) add an environment variable
`OTEL_JAVA_AGENT` with the absolute path of the agent.

The variable can also be set in your `.env` file.

### GitHub Repositories

This plugin has a utility extension installed on the
repository handler, meaning you can easily add GitHub
Packages repositories to your project.

<!-- @formatter:off -->
```kotlin
repositories {
  github.use("octocat", "hello-world")
  github.use("foobar")
}
```
<!-- @formatter:on -->

`github.use()` takes one required parameter and one
optional. The first parameter is required and is the owner
of a repository. Usually a user or an organization. The
second parameter is optional and is the name of the
repository. If this parameter is not set, all repositories
by the owner will be added.

#### Maven Publish integration

If you want to publish a package to GitHub Packages, you
can use the `github.actions()` method to add the current
repository within a GitHub Actions workflow.

<!-- @formatter:off -->
```groovy
plugins {
  id 'maven-publish'
  id 'sh.tnn' version '<tnn-gradle-version>'
}

publishing {
  repositories {
    github.actions()
  }
  publications {
    maven(MavenPublication) {
      from components.java
    }
  }
}
```

Or in Kotlin:

```kotlin
plugins {
  id("maven-publish")
  id("sh.tnn") version "<tnn-gradle-version>"
}

publishing {
  repositories.github.actions()
  publications.create<MavenPublication>("maven") {
    from(components["kotlin"])
  }
}
```

### Telenor Repositories

This plugin has a utility extension installed on the
repository handler, meaning you can easily add Telenor
repositories to your project.

<!-- @formatter:off -->
```kotlin
repositories {
  telenor.public()
  telenor.internal()
  telenor.github()
  telenor.dcapi()
}
```
<!-- @formatter:on -->

`telenor.public()` will add all repositories from
[github.com/telenornorway](https://github.com/telenornorway).
But, you can however, single out specific repositories by
passing in a list of repository
names: `telenor.public("repo1", "repo2", "repo3")`.

The same goes for `telenor.internal()`, but this will
instead point to
the [github.com/telenornorgeinternal](https://github.com/telenornorgeinternal)
organization.

`telenor.github()` will add all repositories from both the
[github.com/telenornorway](https://github.com/telenornorway)
and [github.com/telenornorgeinternal](https://github.com/telenornorgeinternal)
organizations.

`telenor.dcapi()` Will add the legacy DC API repository from
Prima Nexus.
