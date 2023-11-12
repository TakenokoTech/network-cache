package tech.takenoko.android.ncache.entity

import com.squareup.moshi.JsonClass
import tech.takenoko.android.ncache.domain.CacheAdapter
import tech.takenoko.android.ncache.domain.CacheAdapter.Companion.fromWrapJson
import tech.takenoko.android.ncache.domain.CacheAdapter.Companion.toWrapJson

@JsonClass(generateAdapter = true)
data class TestResponse(
    val id: Int,
) {
    object Adapter : CacheAdapter<TestResponse> {
        override fun toJson(v: TestResponse) = toWrapJson(v)
        override fun fromJson(str: String) = fromWrapJson<TestResponse>(str)
    }
}
