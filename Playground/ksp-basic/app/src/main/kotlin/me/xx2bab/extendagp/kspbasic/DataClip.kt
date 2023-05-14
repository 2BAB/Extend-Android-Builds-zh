package me.xx2bab.extendagp.kspbasic

annotation class DataClip(
    // The data-class name you want to generate.
    val className: String,

    // To specify properties you would like to include from the original data class.
    val include: Array<String>,

    // To specify properties you would like to exclude from the original data class,
    // when include and exclude are both set, exclude rules will be ignored.
    val exclude: Array<String> = []
)
