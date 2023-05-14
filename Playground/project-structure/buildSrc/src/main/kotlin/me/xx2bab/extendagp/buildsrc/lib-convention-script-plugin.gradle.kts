plugins {
    id("com.android.library")
}

android {
    lint {
        abortOnError = false
    }
}

println("The lib-convention-script-plugin from ./buildSrc is applied.")