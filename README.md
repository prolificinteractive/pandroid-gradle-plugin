![Build Status](https://travis-ci.org/prolificinteractive/pandroid-gradle-plugin.svg?branch=master)
![Version](https://jitpack.io/v/prolificinteractive/pandroid-gradle-plugin.svg)
![License](https://img.shields.io/badge/license-Prolific_Interactive-blue.svg)

![PAndroid Gradle Plugin](art/logo.png)

# Overview

The PAndroid Gradle plugin allows all Prolific's Android project to run on our CI pipeline for different build variants.

# How to

**Step 1.** Add the JitPack repository to your build file

```groovy
buildscript {
  repositories {
    maven { url 'https://jitpack.io' }
  }
  dependencies {
    classpath 'com.github.prolificinteractive:pandroid-gradle-plugin:master-SNAPSHOT'
  }
}
```

**Step 2.** Apply the plugin in your app's build file.

```groovy
apply plugin: 'com.android.application'
apply plugin: 'pandroid'
```

# Local Setup

The repo has an `app` module to test the plugin locally. To do so, in the root `settings.gradle`, un-comment `include ':app'`, then the run following command to build the plugin and upload it to the local repository located at the root of the project under `repo`.

```bash
$> ./gradlew :pandroid-plugin:uploadArchives -c settings-local.gradle
```

You will need to execute the above command each time you make changes to it in order to get the latest changes applied to the application.

# Builds

This plugin will add four gradle task to your project:
- alphaBuild: build a hockey alpha build and outputs the apk to `ci/alpha.apk`
- betaBuild: build a hockey beta build and outputs the apk to `ci/beta.apk`
- release: build a production build and outputs the apk to `ci/release.apk`
- ciBuild: runs all above tasks one after another.

If the build task has proguard enabled, the mapping files will be moved to the ci folder under a sub-folder named following the apk's name.
Here is an example of a ci folder after running a `ciBuild` with `beta` and `release` having proguard enabled:

```
ci
|-- alpha.apk
|-- beta
|   |-- dump.txt
|   |-- mapping.txt
|   |-- seeds.txt
|   `-- usage.txt
|-- beta.apk
|-- output.json
|-- release
|   |-- dump.txt
|   |-- mapping.txt
|   |-- seeds.txt
|   `-- usage.txt
`-- release.apk
```

# Configuration

Each build task has a default assemble task as following:
- alphaBuild   : `:app:assembleAlpha`
- betaBuild    : `:app:assembleBeta`
- releaseBuild : `:app:assembleRelease`

The default behavior can be overwritten by specifying which assemble task you want the build process to execute in your app's build.gradle:

```groovy
pandroid {
  alphaTask   = ':app:assembleInternalDebug'
  betaTask    = ':app:assembleInternalRelease'
  releaseTask = ':app:assembleProdRelease'
}
```

## License

    MIT License
    
    Copyright (c) 2018 Prolific Interactive
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.