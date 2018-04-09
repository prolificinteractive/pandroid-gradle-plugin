package io.prolific.pandroid.keiko

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class KeikoTask extends DefaultTask {
  KeikoTask() {
    group = 'pandroid'
    description = 'Runs all keiko related tasks'
  }

  @TaskAction def keiko() {}
}
