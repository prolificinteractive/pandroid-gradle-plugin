package io.prolific.pandroid.ci

class BuildTaskTest extends GroovyTestCase {
  void testApkFolder() {
    assertEquals("app/build/outputs/apk/debug",
        BuildTask.apkFolder(":app:assembleDebug").path)
    assertEquals("app/build/outputs/apk/internal/debug",
        BuildTask.apkFolder(":app:assembleInternalDebug").path)
  }

  void testMappingFolder() {
    assertEquals("app/build/outputs/mapping/debug",
        BuildTask.mappingFolder(":app:assembleDebug").path)
    assertEquals("app/build/outputs/mapping/internal/debug",
        BuildTask.mappingFolder(":app:assembleInternalDebug").path)}
}
