package io.prolific.pandroid.bootstrap

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class BootstrapTask extends DefaultTask {
  BootstrapTask() {
    group = 'pandroid'
    description = 'Bootstraps the project by fetching all required keys from Dropbox'
  }

  @TaskAction def bootstrap() {

    def versionFile = new File("version.properties")
    if (versionFile.exists()) versionFile.delete()
    versionFile.withWriter { it.write("BUILD_NUMBER = 1\nVERSION = dev\n") }

    def pandroidDropboxToken = System.getenv().get("PANDROID_DROPBOX_TOKEN")
    if (pandroidDropboxToken == null) {
      throw new GradleException(
          "You need to export the PANDROID_DROPBOX_TOKEN to your system environment before running the bootstrap task.\n" +
              "Refer to the plugin README on how to generate your token:\n" +
              "\$> export PANDROID_DROPBOX_TOKEN=<token>")
    }

    String dropboxFolder = project.extensions.pandroid.dropboxFolder
    println("Bootstrapping from: $dropboxFolder")

    def config = new DbxRequestConfig("PAndroid Gradle Plugin", "en_US")
    def client = new DbxClientV2(config, pandroidDropboxToken)
    def archive = "dropbox.zip"
    client.files()
        .downloadZip("/$dropboxFolder")
        .download(new FileOutputStream(archive))

    def localFolder = dropboxFolder.substring(dropboxFolder.indexOf("/") + 1)
    println("Unzipping... $localFolder")
    "unzip -o $archive -d .".execute().waitForProcessOutput(System.out, System.err)

    def localFile = new File(localFolder)
    FileUtils.copyDirectory(localFile, new File("."))
    localFile.deleteDir()
    new File(archive).delete()
  }
}
