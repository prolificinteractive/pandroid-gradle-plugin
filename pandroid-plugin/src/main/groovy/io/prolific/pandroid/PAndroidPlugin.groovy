package io.prolific.pandroid

import io.prolific.pandroid.ci.AlphaBuildTask
import io.prolific.pandroid.ci.BetaBuildTask
import io.prolific.pandroid.ci.ReleaseBuildTask
import io.prolific.pandroid.vcs.CommitCheckTask
import io.prolific.pandroid.vcs.VcsCheckTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class PAndroidPluginExtension {
  String alphaTask = ":app:assembleAlpha"
  String betaTask = ":app:assembleBeta"
  String releaseTask = ":app:assembleRelease"
}

class PAndroidPlugin implements Plugin<Project> {

  @Override void apply(final Project project) {
    project.extensions.add("pandroid", PAndroidPluginExtension)

    project.task('commitCheck', type: CommitCheckTask)
    project.task('vcsCheck', type: VcsCheckTask)

    project.task('alphaBuild', type: AlphaBuildTask)
    project.task('betaBuild', type: BetaBuildTask)
    project.task('releaseBuild', type: ReleaseBuildTask)
    project.task('ciBuild', dependsOn: ['alphaBuild', 'betaBuild', 'releaseBuild']) {
      group = 'pandroid'
      description = 'Runs all possible build variants needed for ci'
    }
  }
}