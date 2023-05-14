package me.xx2bab.buildinaction.slacktest.model

data class SlackMessageRespBody(
    val ok: Boolean,
    val error: String?
    // There are some more fields from http response,
    // but as a demo we do not require them...
)