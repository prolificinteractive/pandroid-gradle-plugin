package io.prolific.pandroid.ci

import org.gradle.api.tasks.TaskAction

class ReleaseBuildTask extends BuildTask {

  ReleaseBuildTask() {
    group = 'pandroid'
    description = 'Build a release APK and moves it to the `ci` folder'
  }

  @TaskAction def releaseBuild() {
    dependsOn.each { build(it, "release.apk") }
  }
}