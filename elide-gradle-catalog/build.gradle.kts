plugins {
  `version-catalog`
  `maven-publish`
  signing
}

val latestElide = findProperty("elide.version")?.toString() ?: error(
  "Please provide the 'elide.version' property in the gradle.properties file or as a command line argument."
)

group = "dev.elide.gradle"
version = findProperty("version")?.toString() ?: error(
  "Please provide the 'version' property in the gradle.properties file."
)

val mainPluginId = "dev.elide"

val allLibs = listOf(
  "core",
  "base",
  "graalvm",
)

catalog {
  versionCatalog {
    version("elide", latestElide)
    plugin("elide", mainPluginId).versionRef("elide")
    allLibs.forEach {
      library(it, "dev.elide", "elide-$it").versionRef("elide")
    }
  }
}

publishing {
  repositories {
    maven {
      url = uri(rootProject.layout.buildDirectory.dir("elide-maven"))
    }
  }

  publications {
    create<MavenPublication>("maven") {
      from(components["versionCatalog"])

      pom {
        name = "Elide Gradle Catalog"
        description = "Provides mapped versions for Elide and related libraries and plugins."
        inceptionYear = "2023"
        url = "https://elide.dev"
      }
    }
  }
}

signing {
  useGpgCmd()
  sign(publishing.publications["maven"])
}
