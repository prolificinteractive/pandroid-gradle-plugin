package io.prolific.pandroid

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class PAndroidPluginTest extends GroovyTestCase {
  void testPluginAddsCommitCheckTaskToProject() {
    Project project = ProjectBuilder.builder().build()
    project.pluginManager.apply 'pandroid'

    assertTrue(project.tasks.vcsCheck instanceof VcsCheckTask)
  }
}
