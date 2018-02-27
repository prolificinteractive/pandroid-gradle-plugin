package io.prolific.pandroid

import io.prolific.pandroid.vcs.CommitCheckTask
import io.prolific.pandroid.vcs.VcsCheckTask
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class PAndroidPluginTest extends GroovyTestCase {
  void testPluginAddsCommitCheckTaskToProject() {
    Project project = ProjectBuilder.builder().build()
    project.pluginManager.apply 'pandroid'

    assertTrue(project.tasks.vcsCheck instanceof VcsCheckTask)
    assertTrue(project.tasks.commitCheck instanceof CommitCheckTask)
  }

  void testHasExtension() {
    Project project = ProjectBuilder.builder().build()
    project.pluginManager.apply 'pandroid'
    assertTrue(project.extensions.pandroid instanceof PAndroidPluginExtension)
    assertEquals(":app:assembleAlpha", project.extensions.pandroid.alphaTask)
  }
}
