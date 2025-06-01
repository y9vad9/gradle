package dev.elide.gradle;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

import java.nio.file.Path;

public class ElideExtension implements ElideExtensionConfig {
    private static final boolean USE_ROOT_FOR_DEPS = true;
    private static final String DEFAULT_DEVROOT = ".dev";
    protected Project activeProject;
    protected boolean enableInstall = false;
    protected boolean useBuildEmbedded = false;
    protected boolean enableJavacIntegration = true;
    protected boolean enableProjectIntegration = true;
    protected boolean enableMavenIntegration = true;
    protected Property<Boolean> doEnableInstall;
    protected Property<Boolean> doEmbeddedBuild;
    protected Property<Boolean> doUseMavenIntegration;
    protected Property<Boolean> doEnableProjects;
    protected Property<Boolean> doEnableJavaCompiler;
    protected Property<Boolean> doResolveElideFromPath;
    protected RegularFileProperty projectManifest;

    @Override
    public Property<Boolean> getEnableInstall() {
        return doEnableInstall;
    }

    @Override
    public Property<Boolean> getEnableEmbeddedBuild() {
        return doEmbeddedBuild;
    }

    @Override
    public Property<Boolean> getEnableMavenIntegration() {
        return doUseMavenIntegration;
    }

    @Override
    public Property<Boolean> getEnableJavaCompiler() {
        return doEnableJavaCompiler;
    }

    @Override
    public Property<Boolean> getEnableProjectIntegration() {
        return doEnableProjects;
    }

    @Override
    public RegularFileProperty getManifest() {
        return projectManifest;
    }

    @Override
    public Property<Boolean> getResolveElideFromPath() {
        return doResolveElideFromPath;
    }

    Path resolveLocalDepsPath() {
        var projectRoot = (USE_ROOT_FOR_DEPS ? activeProject.getRootProject() : activeProject)
                .getLayout()
                .getProjectDirectory();

        return projectRoot.dir(DEFAULT_DEVROOT)
                .getAsFile()
                .toPath()
                .resolve("dependencies")
                .resolve("m2");
    }

    ElideExtension(Project project, ObjectFactory objects) {
        this.activeProject = project;
        this.doEnableInstall = objects.property(Boolean.class).convention(enableInstall);
        this.doEmbeddedBuild = objects.property(Boolean.class).convention(useBuildEmbedded);
        this.doUseMavenIntegration = objects.property(Boolean.class).convention(enableMavenIntegration);
        this.doEnableProjects = objects.property(Boolean.class).convention(enableProjectIntegration);
        this.doEnableJavaCompiler = objects.property(Boolean.class).convention(enableJavacIntegration);
        this.doResolveElideFromPath = objects.property(Boolean.class).convention(false);
        this.projectManifest = objects.fileProperty().convention(project.getLayout().getProjectDirectory().file("elide.pkl"));
    }
}
