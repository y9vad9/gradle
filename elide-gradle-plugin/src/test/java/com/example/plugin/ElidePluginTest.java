package com.example.plugin;

import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.api.Project;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;


public class ElidePluginTest {
    @Test
    public void pluginDoesntFailTheBuild() {
        // Create a test project and apply the plugin
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("dev.elide");

        // Verify the result
        // assertNotNull(project.getTasks().findByName("tasks"));
    }
}
