package tech.takenoko.android.ncache.entity

import tech.takenoko.android.ncache.domain.CacheAdapter
import java.security.MessageDigest

sealed class NetworkCacheKey<T>(private val value: String, val adapter: CacheAdapter<T>) {

    data class ApiRepository(
        private val num: Int
    ) : NetworkCacheKey<JsonPlaceholderFetchResponse>(
        "ApiRepository${num}",
        JsonPlaceholderFetchResponse.Adapter
    )

    data class TestRepository(
        private val value: String
    ) : NetworkCacheKey<TestResponse>(
        value,
        TestResponse.Adapter
    )

    data object SampleMap : NetworkCacheKey<SimpleMap>(
        "map",
        SimpleMapAdapter
    )

    data object JsonMap : NetworkCacheKey<SimpleMap>(
        "json-map",
        SimpleMapAdapter
    )

    val md5: String
        get() {
            val md = MessageDigest.getInstance("MD5")
            val digest = md.digest(value.toByteArray())
            return digest.joinToString("") { "%02x".format(it) }
        }
}
