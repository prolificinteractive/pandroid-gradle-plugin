package io.prolific.pandroid.ci

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class BetaBuildTask extends DefaultTask {

  BetaBuildTask() {
    group = 'pandroid'
    description = 'Build a beta APK and moves it to the `ci` folder'
  }

  @TaskAction def betaBuild() {
    def buildTask = project.extensions.pandroid.betaTask
    def process = "./gradlew $buildTask".execute()
    process.waitFor()
    println(process.in.text.toString())
    println(process.err.text.toString())

    def apkFolder = AssembleTaskHelper.apkFolder(buildTask)
    def ciFolder = new File("ci")

    FileUtils.copyDirectory(apkFolder, ciFolder)
    AssembleTaskHelper.renameApk(ciFolder, apkFolder, "beta.apk")
  }
}