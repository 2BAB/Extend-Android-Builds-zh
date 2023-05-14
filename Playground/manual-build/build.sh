#!/usr/bin/env bash

# This build script bases on tools below:
#
# Android SDK:
#
# - /android-sdk-path/build-tools/30.0.3
#    - aapt2
#    - d8
#    - zipalign
#
# - /android-sdk-path/platforms/android-30
#    - android.jar
#
# - /android-sdk-path/tools/lib
#    - sdklib (ApkBuilderMain & ApkBuilder)
#    - common
#    - some other libs that provide the dependencies for sdklib and common
#
# JDK:
#
# - /jdk-path/Contents/Home/bin/
#    - java
#    - javac
#    - jarsigner
#
# *nix shell commands
#
#    - echo
#    - which
#    - rm
#    - mkdir
#    - unzip
#    - find
#    - tr
#
# please set them up before you run the script.

## 1. Let's get started
    echo "[PG][ManualBuild] Start"
    ### mkdir build dir
    buildDir="./out"
    echo "[PG][ManualBuild] Cleaning"
    rm -rf ${buildDir}
    mkdir ${buildDir}
    echo "[PG][ManualBuild] Created a build directory at ${buildDir}"
    ### Global params, please set up the Android SDK 30 with its build-tools into system environment (the $PATH)
    sdk=$(dirname $(dirname $(dirname $(which aapt2))))
    androidJar=${sdk}/platforms/android-30/android.jar


## 2. Build resources
    echo "[PG][ManualBuild] Compile resources"
    appResDir="./src/main/res"
    manifest="./src/main/AndroidManifest.xml"
    compileTargetArchive=${buildDir}/compiledRes
    compileTargetArchiveUnzip=${buildDir}/compiledResDir
    linkTarget=${buildDir}/res.apk
    r=${buildDir}/r
    ### Compile
    echo "[PG][ManualBuild] AAPT2 compiling"
    aapt2 compile -o ${compileTargetArchive} --dir ${appResDir}
    unzip -q ${compileTargetArchive} -d ${compileTargetArchiveUnzip}
    echo -e "aapt2 intermediates \r\n  - compiled resources zip archive : ${compileTargetArchive} \r\n  - unzip resources from above     : ${compileTargetArchiveUnzip} "
    ### Link
    echo "[PG][ManualBuild] AAPT2 Linking"
    linkInputs=$(find ${compileTargetArchiveUnzip} -type f | tr '\r\n' ' ')
    aapt2 link -o ${linkTarget} -I ${androidJar} --manifest ${manifest} --java ${r} ${linkInputs}
    echo -e "[PG][ManualBuild] aapt2 generated \r\n  - R.java : ${r} \r\n  - res package : ${linkTarget}"


## 3. Compile Java classes
    echo "[PG][ManualBuild] Compile classes"
    classesOutput=${buildDir}/classes
    mainClassesInput="./src/main/java/me/xx2bab/buildinaction/manualbuild/*.java"
    rDotJava=${r}/me/xx2bab/buildinaction/manualbuild/R.java
    mkdir ${classesOutput}
    echo "[PG][ManualBuild] Created a classes output directory at ${classesOutput}"
    ## .java -> .classes
    echo "[PG][ManualBuild] javac (.java -> .classes)"
    javac -d ${classesOutput} ${mainClassesInput} ${rDotJava} -classpath ${androidJar}
    echo "[PG][ManualBuild] javac generated ${classesOutput}"
    ## .classes -> .dex
    echo "[PG][ManualBuild] D8 (.classes -> .dex)"
    dexOutput=${buildDir}/dex
    mkdir ${dexOutput}
    d8 ${classesOutput}/me/xx2bab/buildinaction/manualbuild/*.class --lib ${androidJar} --output ${dexOutput}
    echo "[PG][ManualBuild] D8 generated ${dexOutput}"


## 4. Build APK
    echo "[PG][ManualBuild] Package and Sign the APK"
    tools=${sdk}/tools/lib
    originApk=${buildDir}/manual-build-unaligned-unsigned.apk
    alignedApk=${buildDir}/manual-build-aligned-unsigned.apk
    zipAlignedSignedApk=${buildDir}/manual-build-aligned-signed.apk
    ## Package APK zip file
    echo "[PG][ManualBuild] Packaging"
    java -cp $(echo ${tools}/*.jar | tr ' ' ':') com.android.sdklib.build.ApkBuilderMain ${originApk} -u -v -z ${linkTarget} -f ${dexOutput}/classes.dex
    echo "[PG][ManualBuild] Built apk by ApkBuilderMain at ${originApk}"
    ## Zipalign the original APK
    echo "[PG][ManualBuild] Zip aligning"
    zaTool=$(dirname $(which aapt2))/zipalign
    ${zaTool} 4 ${originApk} ${alignedApk}
    echo "[PG][ManualBuild] APK aligned ${alignedApk}"
    ## Sign the aligned APK
    echo "[PG][ManualBuild] Signing"
    apksigner sign --ks ./debug.keystore --ks-key-alias androiddebugkey --ks-pass pass:android --key-pass pass:android --out ${zipAlignedSignedApk} ${alignedApk}
    echo "[PG][ManualBuild] APK signed"
    echo "[PG][ManualBuild] Build completed, check the final apk at ${zipAlignedSignedApk}"
