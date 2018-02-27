package io.prolific.pandroid.vcs

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class CommitCheckTask extends DefaultTask {

  CommitCheckTask() {
    group = 'pandroid'
    description = 'Verify that the last commit conforms to PAndroid standard format'
  }

  @TaskAction def checkCommit() {
    def process = "git log --pretty=oneline HEAD^..HEAD".execute()
    process.waitFor()
    def commit = process.in.text.toString()

    def allowedCommitTypes = ['Bug', 'Chore', 'Feature', 'Fix']
    if (!CommitValidator.isCommitValid(commit)) {
      CommitValidator.logError(commit, logger)
      throw new GradleException('Commit Check: Failed')
    }
  }
}