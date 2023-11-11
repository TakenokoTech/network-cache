package tech.takenoko.android.ncache.entity

import tech.takenoko.android.ncache.domain.JsonParseException
import tech.takenoko.android.ncache.utils.moshi

interface CacheAdapter<T> {
    fun toJson(v: T): String
    fun fromJson(str: String): T

    companion object {
        inline fun <reified T> toWrapJson(obj: T): String {
            val json = moshi.adapter(T::class.java).toJson(obj)
            return json ?: throw JsonParseException()
        }

        inline fun <reified T> fromWrapJson(json: String): T {
            val obj = moshi.adapter(T::class.java).fromJson(json)
            return obj ?: throw JsonParseException()
        }
    }
}
