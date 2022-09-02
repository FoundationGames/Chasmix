package foundationgames.chasmix.gradle.test

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification

import java.nio.file.Paths

class ChasmixGradleTest extends Specification {
    File testProject = Paths.get("src/test/resources/gradle/project/").toFile()
    File chasmixBuild = Paths.get("build/libs/").toFile()
    File buildscript = new File(testProject, "build.gradle")

    String args = "build"

    def "Generate Chasm transformers from Mixins on Gradle build"() {
        when:
        if (!buildscript.exists()) { // TODO: Better solution
            buildscript <<
"""plugins {
    id 'java'
    id 'foundationgames.chasmix.gradle'
}

repositories {
    flatDir {
        dirs '${chasmixBuild.absolutePath.replaceAll("\\\\", "/")}'
    }
}

dependencies {
    implementation 'foundationgames.chasmix:chasmix-0.1'
}
""" // TODO: Make version not hardcoded ^
        }

        def res = GradleRunner.create()
            .withArguments(this.args)
            .withProjectDir(this.testProject)
            .forwardOutput()
            .withPluginClasspath()
        .build()

        then:
        // TODO: Test that the result jar actually contains valid Chasm transformers
        res.task(":${this.args}").outcome != TaskOutcome.FAILED
    }
}
