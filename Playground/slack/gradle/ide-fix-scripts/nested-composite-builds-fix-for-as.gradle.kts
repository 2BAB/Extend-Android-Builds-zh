/**
 * This script is to give a workaround for Android Studio
 * which does not support nested composite-builds.
 * For example, a composite-builds chain like below:
 * app -> plugin-config -> slack -> build-env
 *
 * IDEA does not require this, but it's not harmful to apply.
 * Check more on below links.
 *
 * @link https://issuetracker.google.com/issues/189366120
 */
//println("Applying fix script fro nested composite-builds.")
includeBuild("plugins/slack")
includeBuild("plugins/slack-lazy")
includeBuild("plugins/slack-nested-blocks")
includeBuild("plugins/slack-task-orchestra")


