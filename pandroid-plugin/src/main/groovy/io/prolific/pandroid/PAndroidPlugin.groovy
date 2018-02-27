package io.prolific.pandroid

import io.prolific.pandroid.ci.AlphaBuildTask
import io.prolific.pandroid.vcs.CommitCheckTask
import io.prolific.pandroid.vcs.VcsCheckTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class PAndroidPluginExtension {
  String alphaTask = ":app:assembleAlpha"
}

class PAndroidPlugin implements Plugin<Project> {

  @Override void apply(final Project project) {
    project.extensions.add("pandroid", PAndroidPluginExtension)
    project.task('commitCheck', type: CommitCheckTask)
    project.task('vcsCheck', type: VcsCheckTask)
    project.task('alphaBuild', type: AlphaBuildTask)
  }
}