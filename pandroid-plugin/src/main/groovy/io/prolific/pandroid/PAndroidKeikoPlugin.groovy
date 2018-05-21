package io.prolific.pandroid

import io.prolific.pandroid.keiko.KeikoTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testing.jacoco.tasks.JacocoReport

class PAndroidKeikoPlugin implements Plugin<Project> {

  @Override void apply(final Project project) {

    project.configurations.all {
      resolutionStrategy {
        eachDependency { details ->
          if ('org.jacoco' == details.requested.group) {
            details.useVersion "0.8.1"
          }
        }
      }
    }

    project.apply plugin: 'jacoco'

    project.configure(project.jacoco, { toolVersion = 'latest.release' })

    project.configure(project) {
      android {
        buildTypes {
          debug {
            testCoverageEnabled = true
          }
        }

        // enable the coverage report for local tests when using version 2.2.+ of Android Gradle plugin
        testOptions.unitTests.all {
          it.jacoco.includeNoLocationClasses = true
        }
      }

      project.afterEvaluate {
        // Grab all build types and product flavors
        def buildTypes = android.buildTypes
            .collect { type -> type.name }
        //.findAll { buildTypeName -> 'debug'.equals(buildTypeName) == false  } // Ideally we should scan for non ignored build variants.
        def productFlavors = android.productFlavors.collect { flavor -> flavor.name }

        // When no product flavors defined, use empty
        if (!productFlavors) productFlavors.add('')

        productFlavors.each { productFlavorName ->
          buildTypes.each { buildTypeName ->
            def sourceName, sourcePath
            if (!productFlavorName) {
              sourceName = sourcePath = "${buildTypeName}"
            } else {
              sourceName = "${productFlavorName}${buildTypeName.capitalize()}"
              sourcePath = "${productFlavorName}/${buildTypeName}"
            }

            String productFlavor = project.rootProject.ext.get("keikoProductFlavor")
            String buildType = project.rootProject.ext.get("keikoBuildType")
            if (buildType.isEmpty()) {
              // Set default build type for tests
              buildType = 'release'
            }

            rootProject.ext.set("keikoBuildType", 'release')
            // Filter out variants that haven't been selected specified.
            if (productFlavorName != productFlavor && productFlavorName != '') {
              return
            }

            if (buildTypeName != buildType) {
              return
            }

            def testTaskName = "test${sourceName.capitalize()}UnitTest"

            // Create coverage task of form 'testFlavorTypeCoverage' depending on 'testFlavorTypeUnitTest'
            Task testCoverageTask = project.task("testCoverage", type: JacocoReport,
                dependsOn: [// Adds a dependency on the corresponding test task
                            "$testTaskName",
                            // Add a dependency on the corresponding lint task
                            "lint${sourceName.capitalize()}",
                            // Add a dependency on the corresponding android UI test task
                            // TODO use now with keiko.dependsOn create{}CoverageReport
                            /* "create${sourceName.capitalize()}CoverageReport" */]) {
              group = 'Reporting'
              description =
                  "Generate Jacoco coverage reports for the ${sourceName.capitalize()} build."

              // UI, "noise", generated classes, platform classes, etc.
              def excludes = ['**/R.class',
                              '**/R$*.class',
                              '**/*$ViewInjector*.*',
                              '**/*$ViewBinder*.*',
                              '**/BuildConfig.*',
                              '**/Manifest*.*',
                              '**/*$Lambda$*.*',
                              '**/*Module.*',
                              '**/*Dagger*.*',
                              '**/*MembersInjector*.*',
                              '**/*_Provide*Factory*.*',
                              '**/*_Factory*.*']

              def javaClassDir = "${project.buildDir}/intermediates/classes/$sourcePath"
              def kotlinClassDir = "${project.buildDir}/tmp/kotlin-classes/$sourceName"
              classDirectories = fileTree(dir: javaClassDir, excludes: excludes) + fileTree(
                  // Kotlin generated classes on Android project (debug build)
                  dir: kotlinClassDir,
                  excludes: excludes)

              def coverageSourceDirs = ['src/main/java',
                                        'src/main/kotlin',
                                        'src/main/res',
                                        'src/main/AndroidManifest.xml',
                                        "src/$productFlavorName/java",
                                        "src/$productFlavorName/kotlin",
                                        "src/$productFlavorName/res",
                                        "src/$productFlavorName/AndroidManifest.xml",
                                        "src/$buildTypeName/java",
                                        "src/$buildTypeName/kotlin",
                                        "src/$buildTypeName/res",
                                        "src/$buildTypeName/AndroidManifest.xml",].findAll {
                !it.contains("//")
              }

              additionalSourceDirs = files(coverageSourceDirs)
              sourceDirectories = files(coverageSourceDirs)
              executionData = fileTree(dir: project.projectDir,
                  includes: ["**/*.exec",
                             '**/*-coverage.ec'])

              reports {
                xml.enabled = true
                html.enabled = true
              }

              coverageSourceDirs.forEach {
                // Create these source folders if they don't already exist.
                new File("${project.projectDir}/$it").mkdirs()
              }

              // Create these source folders if they don't already exist.
              new File("${project.projectDir}/src/test/java").mkdirs()
              new File("${project.projectDir}/src/androidTest/java").mkdirs()
              new File("${project.projectDir}/src/test/kotlin").mkdirs()
              new File("${project.projectDir}/src/androidTest/kotlin").mkdirs()

              sonarqube {

                androidVariant sourceName

                properties {
                  // https://docs.sonarqube.org/display/PLUG/Java+Plugin+and+Bytecode
                  def libraries = fileTree(dir: project.android.sdkDirectory.getPath(),
                      includes: ['platforms/android-27/android.jar']) + fileTree(
                      dir: project.buildDir,
                      includes: ['intermediates/classes-jar/**/classes.jar'])

                  property "sonar.java.libraries", libraries
                  property "sonar.java.test.libraries", libraries

                  property "sonar.exclusions", "build/**,**/*.png,*.iml, **/*generated*"
                  property "sonar.import_unknown_files", true

                  property "sonar.sourceEncoding", "UTF-8"
                  property "sonar.sources", coverageSourceDirs.join(",")
                  property "sonar.java.binaries", "$javaClassDir,$kotlinClassDir"
                  property "sonar.tests", "src/test/,src/androidTest/"
                  // where the tests are located
                  property "sonar.java.test.binaries", "$javaClassDir,$kotlinClassDir"
                  property "sonar.scm.provider", "git"
                  property "sonar.jacoco.reportPaths", fileTree(dir: project.projectDir,
                      includes: ['**/*.exec'])
                  property "sonar.java.coveragePlugin", "jacoco"
                  property "sonar.jacoco.itReportPath", fileTree(dir: project.projectDir,
                      includes: ['**/*-coverage.ec'])
                  property "sonar.android.lint.report",
                      "${project.buildDir}/reports/lint-results-${sourceName}.xml"
                }
              }
            }

            Task rootTask = project.rootProject.tasks.getByName("keiko")
            rootTask.dependsOn testCoverageTask
          }
        }
      }
    }
  }
}

