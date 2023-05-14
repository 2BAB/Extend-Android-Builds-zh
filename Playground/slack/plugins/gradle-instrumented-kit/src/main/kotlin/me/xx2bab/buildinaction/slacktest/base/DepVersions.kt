package me.xx2bab.buildinaction.slacktest.base

import java.io.File

class DepVersions {

    val versions = File("../../deps.versions.toml").readText()
    val regexPlaceHolder = "%s\\s\\=\\s\\\"([A-Za-z0-9\\.\\-]+)\\\""
    fun getVersion(versionKey: String) = regexPlaceHolder.format(versionKey).toRegex().find(versions)!!.groupValues[1]

}