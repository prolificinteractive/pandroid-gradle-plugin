package io.prolific.pandroid.ci

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ReleaseBuildTask extends DefaultTask {

  ReleaseBuildTask() {
    group = 'pandroid'
    description = 'Build a release APK and moves it to the `ci` folder'
  }

  @TaskAction def releaseBuild() {
    def buildTask = project.extensions.pandroid.releaseTask
    def process = "./gradlew $buildTask".execute()
    process.waitFor()
    println(process.in.text.toString())
    println(process.err.text.toString())

    def apkFolder = AssembleTaskHelper.apkFolder(buildTask)
    def ciFolder = new File("ci")

    FileUtils.copyDirectory(apkFolder, ciFolder)
    AssembleTaskHelper.renameApk(ciFolder, apkFolder, "release.apk")
  }
}