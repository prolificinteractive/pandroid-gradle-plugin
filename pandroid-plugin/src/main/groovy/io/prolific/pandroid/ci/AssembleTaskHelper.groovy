package io.prolific.pandroid.ci

import org.gradle.api.GradleException

import java.util.regex.Pattern

final class AssembleTaskHelper {

  static File apkFolder(final String taskName) {
    def pattern = Pattern.compile("^:(.*):assemble(.*)\$")
    def matcher = pattern.matcher(taskName)

    if (!matcher.find()) {
      throw new GradleException("Cant map $taskName to a specific apk path")
    }
    def projectName = matcher.group(1)
    def variant = matcher.group(2)
    return new File(
        "${projectName}/build/outputs/apk${variant.replaceAll("([A-Z])", "/\$1").toLowerCase()}/")
  }

  static File mappingFolder(final String taskName) {
    def pattern = Pattern.compile("^:(.*):assemble(.*)\$")
    def matcher = pattern.matcher(taskName)

    if (!matcher.find()) {
      throw new GradleException("Cant map $taskName to a specific mapping path")
    }
    def projectName = matcher.group(1)
    def variant = matcher.group(2)
    return new File(
        "${projectName}/build/outputs/mapping${variant.replaceAll("([A-Z])", "/\$1").toLowerCase()}/")
  }

  static void renameApk(File ciFolder, File apkFolder, outputApkFile) {
    def outputFile = new File("${apkFolder.path}/output.json")
    outputFile.withReader {
      it.eachLine {
        def matcher = Pattern.compile("\"path\":\"(.*).apk\"").matcher(it)
        if (matcher.find()) {
          new File("${ciFolder.path}/${matcher.group(1)}.apk").renameTo(
              "${ciFolder.path}/$outputApkFile")
        }
      }
    }
  }
}