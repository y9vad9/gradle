//
// Elide Gradle Plugin
//

val thisVersion = "1.0.0"

pluginManagement {
    // Installs Elide's maven repository for plugin resolution.
    repositories {
        maven {
            name = "elide"
            url = uri("https://maven.elide.dev")
        }
    }
}

dependencyResolutionManagement {
    // Installs Elide's maven repository for dependency resolution.
    @Suppress("UnstableApiUsage")
    repositories {
        maven {
            name = "elide"
            url = uri("https://maven.elide.dev")
        }
    }

    // Installs Elide's Gradle Version Catalog for dependency management.
    versionCatalogs {
        create("elideRuntime") {
            from("dev.elide.gradle:elide-gradle-catalog:$thisVersion")
        }
    }
}
