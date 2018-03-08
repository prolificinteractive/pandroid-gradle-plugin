package io.prolific.pandroid.keiko

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class KeikoTask extends DefaultTask {
  KeikoTask() {
    group = 'pandroid'
    description = 'Runs all keiko related tasks'
  }

  @TaskAction def keiko() {
    if (!project.plugins.hasPlugin("org.sonarqube")) {
      throw new GradleException(
          "Failed running :keiko on a project that does not have the `org.sonarqube` plugin applied to it")
    }
    def process = "./gradlew sonarqube".execute()
    process.waitForProcessOutput(System.out, System.err)
  }
}
