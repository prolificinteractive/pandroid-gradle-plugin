package io.prolific.pandroid

import org.gradle.api.logging.Logger

class CommitValidator {
  private CommitValidator() {
    throw new IllegalAccessException("No instances.")
  }

  static boolean isCommitValid(final String commit) {
    def allowedCommitTypes = ['Bug', 'Chore', 'Feature', 'Fix', 'Test']
    def split = commit.split(' ')
    return !(split.size() < 4 || !allowedCommitTypes.contains(split[1]) || split[2] != '-')
  }

  static void logError(final String commit, final Logger logger) {
    def process = "git log -1 --format='%ae' ${commit.split(' ')[0]}".execute()
    process.waitFor()
    def authorName = process.in.text.toString()
    logger.error("")
    logger.error("Wrong commit format:")
    logger.error("--> BEGIN:")
    logger.error("$commit")
    logger.error("author: $authorName<-- END")
  }
}