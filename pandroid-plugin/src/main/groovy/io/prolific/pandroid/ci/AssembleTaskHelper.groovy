package io.prolific.pandroid.ci

import org.gradle.api.GradleException

import java.util.regex.Pattern

final class AssembleTaskHelper {

  static String apkFolder(final String taskName) {
    def pattern = Pattern.compile("^:(.*):assemble(.*)\$")
    def matcher = pattern.matcher(taskName)

    if (!matcher.find()) {
      throw new GradleException("Cant map $taskName to a specific apk path")
    }
    def projectName = matcher.group(1)
    def variant = matcher.group(2)
    return "${projectName}/build/outputs/apk${variant.replaceAll("([A-Z])", "/\$1").toLowerCase()}/"
  }
}