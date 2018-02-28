package io.prolific.pandroid.ci

import org.gradle.api.tasks.TaskAction

class AlphaBuildTask extends BuildTask {

  AlphaBuildTask() {
    group = 'pandroid'
    description = 'Build an alpha APK and moves it to the `ci` folder'
  }

  @TaskAction def alphaBuild() {
    build(project.extensions.pandroid.alphaTask, "alpha.apk")
  }
}