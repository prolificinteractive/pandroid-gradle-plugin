package io.prolific.pandroid.ci

import org.gradle.api.tasks.TaskAction

class BetaBuildTask extends BuildTask {

  BetaBuildTask() {
    group = 'pandroid'
    description = 'Build a beta APK and moves it to the `ci` folder'
  }

  @TaskAction def betaBuild() {
    dependsOn.each { build(it, "beta.apk") }
  }
}