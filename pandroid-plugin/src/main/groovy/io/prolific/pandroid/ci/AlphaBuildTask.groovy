package io.prolific.pandroid.ci

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AlphaBuildTask extends DefaultTask {

  AlphaBuildTask() {
    group = 'pandroid'
    description = 'Build an alpha build and moves it to the `ci` folder'
  }

  @TaskAction def alphaBuild() {
    def process = "./gradlew ${project.extensions.pandroid.alphaTask}".execute()
    process.waitFor()
    println(process.in.text.toString())
    println(process.err.text.toString())
  }
}