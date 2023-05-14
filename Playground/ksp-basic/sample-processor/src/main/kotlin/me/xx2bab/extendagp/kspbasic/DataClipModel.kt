package me.xx2bab.extendagp.kspbasic

data class DataClipModel(
    var className: String = "",
    var mode: ClipMode = ClipMode.INCLUDE,
    val includes: HashSet<String> = HashSet(),
    val excludes: HashSet<String> = HashSet()
) {
    fun isRequired(target: String): Boolean {
        if (mode == ClipMode.INCLUDE) {
            return includes.contains(target)
        } else {
            return !excludes.contains(target)
        }
    }
}

enum class ClipMode {
    INCLUDE, EXCLUDE
}
