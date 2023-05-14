package me.xx2bab.extendagp.kspbasic


@DataClip(className = "LoginUser", include = ["firstName", "lastName", "nick"])
data class User(
    val firstName: String,
    val lastName: String,
    val nick: String,
    val id: String,
    val email: String
)
