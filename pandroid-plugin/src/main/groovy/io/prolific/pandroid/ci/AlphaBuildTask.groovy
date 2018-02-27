package io.prolific.pandroid.ci

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AlphaBuildTask extends DefaultTask {

  AlphaBuildTask() {
    group = 'pandroid'
    description = 'Build an alpha APK and moves it to the `ci` folder'
  }

  @TaskAction def alphaBuild() {
    def buildTask = project.extensions.pandroid.alphaTask
    def process = "./gradlew $buildTask".execute()
    process.waitFor()
    println(process.in.text.toString())
    println(process.err.text.toString())

    def apkFolder = AssembleTaskHelper.apkFolder(buildTask)
    def ciFolder = new File("ci")

    FileUtils.copyDirectory(apkFolder, ciFolder)
    AssembleTaskHelper.renameApk(ciFolder, apkFolder, "alpha.apk")
  }
}