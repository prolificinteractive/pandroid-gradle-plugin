package io.prolific.pandroid.vcs

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class VcsCheckTask extends DefaultTask {

  VcsCheckTask() {
    group = 'pandroid'
    description = 'Prints out the percentage of commits conforming to PAndroid standard format'
  }

  @TaskAction def checkCommit() {
    def process = "git log --pretty=oneline".execute()
    process.waitFor()
    def commits = process.in.text.toString()
    def lines = 0
    def invalidCommits = 0
    commits.eachLine { commit ->
      if (!CommitValidator.isCommitValid(commit)) {
        CommitValidator.logError(commit, logger)
        ++invalidCommits
      }
      ++lines
    }
    logger.println("VcsCheck: ${((lines - invalidCommits) * 100 / lines).toFloat().trunc(2)}%")
  }
}