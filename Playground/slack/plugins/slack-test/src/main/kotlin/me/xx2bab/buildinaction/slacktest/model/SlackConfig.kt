package me.xx2bab.buildinaction.slacktest.model

data class SlackConfig(val slackEndpoint: String = "https://slack.com/api/",
                       var token: String)