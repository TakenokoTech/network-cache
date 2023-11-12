package tech.takenoko.android.ncache.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tech.takenoko.android.ncache.domain.CacheAdapter
import tech.takenoko.android.ncache.domain.CacheAdapter.Companion.fromWrapJson
import tech.takenoko.android.ncache.domain.CacheAdapter.Companion.toWrapJson

@JsonClass(generateAdapter = true)
data class JsonPlaceholderFetchResponse(
    val userId: Int,
    val id: Int,
    // val title: String,
    // val completed: Boolean,

    @field:Json(ignore = true)
    val isCache: Boolean = false
) {
    object Adapter : CacheAdapter<JsonPlaceholderFetchResponse> {
        override fun toJson(v: JsonPlaceholderFetchResponse) = toWrapJson(v)
        override fun fromJson(str: String) = fromWrapJson<JsonPlaceholderFetchResponse>(str)
    }
}
