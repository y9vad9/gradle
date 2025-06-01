package dev.elide.gradle;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;

public interface ElideExtensionConfig {
    Property<Boolean> getEnableInstall();
    Property<Boolean> getEnableEmbeddedBuild();
    Property<Boolean> getEnableMavenIntegration();
    Property<Boolean> getEnableJavaCompiler();
    Property<Boolean> getEnableProjectIntegration();
    RegularFileProperty getManifest();
    Property<Boolean> getResolveElideFromPath();
}
