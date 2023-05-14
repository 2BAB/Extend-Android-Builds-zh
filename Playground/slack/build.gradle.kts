// Once Android Studio or IDEA got issues fixed, we can remove corresponding script below,
// Gradle can run without these fixes.
//apply(from = "gradle/ide-fix-scripts/action-transform-fix-for-both-ide-root-build.gradle.kts")
task("clean") {
    delete(rootProject.buildDir)
}
