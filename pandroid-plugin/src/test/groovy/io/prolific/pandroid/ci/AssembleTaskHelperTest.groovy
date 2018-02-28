package io.prolific.pandroid.ci

class AssembleTaskHelperTest extends GroovyTestCase {
  void testApkFolder() {
    assertEquals("app/build/outputs/apk/debug",
        AssembleTaskHelper.apkFolder(":app:assembleDebug").path)
    assertEquals("app/build/outputs/apk/internal/debug",
        AssembleTaskHelper.apkFolder(":app:assembleInternalDebug").path)
  }
}
