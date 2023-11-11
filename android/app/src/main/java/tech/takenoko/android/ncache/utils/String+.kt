package tech.takenoko.android.ncache.utils

import java.security.MessageDigest

fun String.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}
