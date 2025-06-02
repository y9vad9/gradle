// Use `latest` for the latest version, or any other tag, branch, or commit SHA on this project.
val elidePluginVersion: String by settings
apply(from = "https://gradle.elide.dev/$elidePluginVersion/elide.gradle.kts")

