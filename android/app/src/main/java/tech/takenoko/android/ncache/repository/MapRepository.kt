package tech.takenoko.android.ncache.repository

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import tech.takenoko.android.ncache.entity.NetworkCacheKey
import tech.takenoko.android.ncache.entity.NetworkCacheResult
import tech.takenoko.android.ncache.entity.SimpleMap
import tech.takenoko.android.ncache.utils.moshi

class MapRepository(
    override val context: Context
) : NetworkCache {
    suspend fun load(key: String) = load(NetworkCacheKey.SampleMap).map { it[key] }
    suspend fun save(map: SimpleMap) = save(NetworkCacheKey.SampleMap, map)

    suspend fun fetch() = runCatching {
        val result = getCachedOrFetch(NetworkCacheKey.JsonMap) {
            jsonPlaceholder.fetch(1)
        }
        result.value.toMutableMap().also {
            it["title"] = it["title"].toString().take(8)
            it["description"] = it["description"].toString().take(8)
            it["isCache"] = result is NetworkCacheResult.Cached
        }
    }

    suspend fun getBoolean(key: String): Boolean? = fetch().getOrNull()?.get(key) as? Boolean
    suspend fun getString(key: String): String? = fetch().getOrNull()?.get(key) as String
    suspend fun getDouble(key: String): Double? = fetch().getOrNull()?.get(key) as? Double
    suspend fun getList(key: String): List<*>? = fetch().getOrNull()?.get(key) as? List<*>
    suspend fun getInt(key: String): Int? = getDouble(key)?.toInt()

    private val jsonPlaceholder
        get() = Retrofit
            .Builder()
            .baseUrl("https://dummyjson.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(JsonPlaceholder::class.java)

    interface JsonPlaceholder {
        @GET("products/{num}")
        suspend fun fetch(@Path("num") num: Int): SimpleMap
    }
}
