package com.example.plugin;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class ElidePluginFunctionalTest {
    @Test
    public void canRunTasks() throws IOException {
        File projectDir = new File("build/functionalTest");
        Files.createDirectories(projectDir.toPath());
        writeString(new File(projectDir, "settings.gradle"), "");
        writeString(new File(projectDir, "build.gradle"),
                """
                    plugins {
                      id('dev.elide')
                    }
                    """);

        GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withArguments("tasks")
            .withProjectDir(projectDir)
            .build();
    }

    @Test
    public void canShimJavac() throws IOException {
        File projectDir = new File("build/functionalTestJavac");
        var helloPathRelative = Paths.get("src/main/java/com/example/HelloWorld.java");
        var helloPath = projectDir.toPath().resolve(helloPathRelative);
        Files.createDirectories(projectDir.toPath());
        writeString(new File(projectDir, "settings.gradle.kts"), "");
        writeString(new File(projectDir, "build.gradle.kts"),
                """
                        plugins {
                          id("dev.elide")
                          java
                        }
                        repositories {
                          mavenCentral()
                        }
                        """);

        Files.createDirectories(helloPath.getParent());
        writeString(new File(projectDir, "src/main/java/com/example/HelloWorld.java"),
                """
                        package com.example;

                        public class HelloWorld {
                            public static void main(String[] args) {
                                System.out.println("Hello, World!");
                            }
                        }
                        """);

        // Run `tasks` (tests configuration phase)
        BuildResult result = GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withArguments("tasks")
                .withProjectDir(projectDir)
                .build();

        assertTrue(result.getOutput().contains("BUILD SUCCESSFUL"));

        BuildResult buildResult = GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withArguments("build", "--info")
                .withProjectDir(projectDir)
                .build();

        assertTrue(buildResult.getOutput().contains("BUILD SUCCESSFUL"));
        assertTrue(buildResult.getOutput().contains("Using Elide "));
    }

    private void writeString(File file, String string) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            writer.write(string);
        }
    }
}
