package io.prolific.pandroid.ci

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException

import java.util.regex.Pattern

abstract class BuildTask extends DefaultTask {

  static void build(String buildTask, String apkName) {
    def process = "./gradlew $buildTask".execute()
    process.waitFor()
    println(process.in.text.toString())
    println(process.err.text.toString())

    def apkFolder = apkFolder(buildTask)
    def mappingFolder = mappingFolder(buildTask)
    def ciFolder = new File("ci")

    FileUtils.copyDirectory(apkFolder, ciFolder)
    renameApk(ciFolder, apkFolder, apkName)
    if (mappingFolder.exists()) {
      FileUtils.copyDirectoryToDirectory(mappingFolder, ciFolder)
    }
  }

  static File apkFolder(final String taskName) {
    def (projectName, variant) = projectNameVariant(taskName)
    return new File(
        "${projectName}/build/outputs/apk${variant.replaceAll("([A-Z])", "/\$1").toLowerCase()}/")
  }

  static File mappingFolder(final String taskName) {
    def (projectName, variant) = projectNameVariant(taskName)
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

  private static List projectNameVariant(String taskName) {
    def pattern = Pattern.compile("^:(.*):assemble(.*)\$")
    def matcher = pattern.matcher(taskName)

    if (!matcher.find()) {
      throw new GradleException("Cant map $taskName to a specific mapping path")
    }
    def projectName = matcher.group(1)
    def variant = matcher.group(2)
    return [projectName, variant]
  }
}