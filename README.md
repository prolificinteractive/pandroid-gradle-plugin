![Build Status](https://travis-ci.org/prolificinteractive/pandroid-gradle-plugin.svg?branch=master)
![Version](https://jitpack.io/v/prolificinteractive/pandroid-gradle-plugin.svg)
![License](https://img.shields.io/badge/license-Prolific_Interactive-blue.svg)

![PAndroid Gradle Plugin](art/logo.png)

# Overview

The PAndroid Gradle plugin allows all Prolific's Android project to run on our CI pipeline for different build variants.

# How to

Add the JitPack repository and apply these two plugins in your top level build file.

```groovy
buildscript {
  repositories {
    maven { url 'https://jitpack.io' }
  }
  dependencies {
    classpath "org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.6.2"
    classpath 'com.github.prolificinteractive:pandroid-gradle-plugin:<latest-version>'
  }
}

apply plugin: 'pandroid'
```

# Local Setup

The repo has an `app` module to test the plugin locally. To do so, run the following command to build the plugin and upload it to the local repository located at the root of the project under `repo`.

```bash
$> make plugin
```

You will need to execute the above command each time you make changes to it in order to get the latest changes applied to the application.

# Builds

This plugin will add four gradle tasks to your project:
- `:alphaBuild`: build a hockey alpha build and outputs the apk to `ci/alpha.apk`
- `:betaBuild`: build a hockey beta build and outputs the apk to `ci/beta.apk`
- `:releaseBuild`: build a production build and outputs the apk to `ci/release.apk`
- `:ciBuild`: runs all above tasks one after another.

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

The PAndroid Gradle plugin will add the necessary tasks to the project but you still have to specify on which task they depend in your app's build.gradle:

```groovy
alphaBuild.dependsOn ':app:assembleDebug'
betaBuild.dependsOn ':app:assembleBeta'
releaseBuild.dependsOn ':app:assembleRelease'
```

These two properties define which product flavor and build type variant keiko will use to run Junit tests, Android Instrumentated tests and Android Lint.

```groovy
rootProject.ext {
    keikoProductFlavor = "prod" // defaults to ''
    keikoBuildType = "release" // defaults to release
  }
```

This plugin also adds a `:bootstrap` task to fetch the keys from Dropbox. You can specify the path to the Dropbox folder as following:

```groovy
pandroid {
  dropboxFolder = 'Hut/alpha'
}
```

First of all, you need access to the dropbox folder used by this plugin. Follow this [link](https://www.dropbox.com/1/oauth2/authorize?client_id=bq8usftpqgqqhmn&response_type=token&redirect_uri=http://localhost) and authorize the application. Once given access, you should see the `access_token` in the url. Then export the access token to your environment variables:

```bash
export PANDROID_DROPBOX_TOKEN=<access_token>
```

And then run:

```bash
$> touch bootstrap.gradle
$> ./gradlew bootstrap -c bootstrap.gradle
```

You need an empty settings file to avoid configuring the entire project which will fail as you do not have the keys yet after a fresh cloning.
Note that the folder with the keys need to be in `/Applications/PAndroid Gradle Plugin/`

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