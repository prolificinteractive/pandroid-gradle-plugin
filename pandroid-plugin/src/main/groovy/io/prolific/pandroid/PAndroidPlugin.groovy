package io.prolific.pandroid

import org.gradle.api.Plugin
import org.gradle.api.Project

class PAndroidPlugin implements Plugin<Project> {
  @Override void apply(final Project project) {
    project.task('commitCheck', type: CommitCheckTask)
    project.task('vcsCheck', type: VcsCheckTask)
  }
}