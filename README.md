## Elide Gradle Plugin

Experimental plugin for using [Elide](https://github.com/elide-dev/elide) from within Gradle.

### Installation

1) Create the `javac` shim in your `JAVA_HOME`:

   **`$JAVA_HOME/bin/elide-javac`**
    ```bash
    #!env bash
    exec elide javac -- "${@}"
    ```

2) Install and use the plugin as shown below.
3) **That's it! Enjoy faster dependency resolution and Java compilation.**

> [!NOTE]
> We hope to eliminate this shim soon.

### Usage

**`gradle.properties`**
```properties
elidePluginVersion=1.0.0-beta5
```

**`settings.gradle.kts`**
```kotlin
// Use `latest` for the latest version, or any other tag, branch, or commit SHA on this project.
val elidePluginVersion: String by settings
apply(from = "https://gradle.elide.dev/tag/$elidePluginVersion/elide.gradle.kts")
```

**`build.gradle.kts`**
```kotlin
plugins {
  alias(elideRuntime.plugins.elide)
}

// Settings here apply on a per-project basis. See below for available settings; all properties
// are optional, and you don't need to include this block at all if you are fine with defaults.
elide {
  // Use Elide's Maven resolver and downloader instead of Gradle's. Defaults to `true` when an
  // `elide.pkl` file is present in the project root.
  enableInstall = true

  // Use Elide to compile Java instead of the stock Compiler API facilities used by Gradle.
  // Defaults to `true` if the plugin is active in the project at all.
  enableJavaCompiler = true

  // Enable Elide project awareness for Gradle. For example, build scripts can show up as runnable
  // exec tasks within the Gradle build.
  enableProjectIntegration = true

  // Set the path to the project manifest, expressed in Pkl format. Elide project manifests can
  // specify dependencies, build scripts, and other project metadata. Defaults to `elide.pkl` and
  // automatically finds any present `elide.pkl` in the active project.
  manifest = layout.projectDirectory.file("elide.pkl")
}
```

### What's this?

Elide is a runtime and batteries-included toolchain for Kotlin/Java, Python, JavaScript, and TypeScript, that can be
used as a drop-in replacement for `javac` (among other tools).

Elide builds `javac` as a native image and includes it within the Elide binary. This plugin changes your Gradle build
(as applicable) to use Elide's toolchain facilities instead of Gradle's built-in ones.

The result can be a significant performance improvement for **fetching dependencies** and **compiling code**.

Learn more about Elide at [elide.dev](https://elide.dev).

### Features

> [!NOTE]
> Elide is in beta, and this plugin is experimental. Use at your own risk. Please report any issues you encounter.

- [x] Provide a Gradle plugin
- [x] Provide a Gradle Catalog
- [x] Support for `elide install` as Gradle's Maven resolver
- [x] Support for `elide javac -- ...` as Gradle's Java compiler
- [x] Use Elide from the user's `PATH`
- [x] Use a local copy of Elide within the project
- [ ] Gradle-level Elide download cache
- [ ] Ability to pin Elide version
- [ ] Support the configuration cache
- [ ] Race-and-report vs. `javac`
- [ ] Augment project metadata for reporting
- [ ] Generate dependency manifests

