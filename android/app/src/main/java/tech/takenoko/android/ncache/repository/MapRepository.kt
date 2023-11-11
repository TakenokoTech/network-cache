package tech.takenoko.android.ncache.repository

import android.content.Context
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import tech.takenoko.android.ncache.entity.CacheAdapter
import tech.takenoko.android.ncache.entity.NetworkCacheResult
import tech.takenoko.android.ncache.utils.moshi

class MapRepository(
    override val context: Context
) : NetworkCache {
    suspend fun load(key: String): Result<Any?> = load("map", adapter).map { it[key] }
    suspend fun save(map: Map<String, Any>): Result<Unit> = save("map", map, adapter)

    suspend fun fetch() = runCatching {
        val result = getCachedOrFetch("json-map", adapter) {
            adapter.fromJson(jsonPlaceholder.fetch(1).string())
        }
        result.value.toMutableMap().also {
            it["title"] = it["title"].toString().take(8)
            it["isCache"] = (result is NetworkCacheResult.Cached).toString()
        }
    }

    suspend fun getBoolean(key: String): Boolean? = fetch().getOrNull()?.get(key) as? Boolean
    suspend fun getString(key: String): String? = fetch().getOrNull()?.get(key) as? String
    suspend fun getDouble(key: String): Double? = fetch().getOrNull()?.get(key) as? Double
    suspend fun getInt(key: String): Int? = getDouble(key)?.toInt()

    private val adapter = object : CacheAdapter<Map<String, Any>> {
        override fun toJson(v: Map<String, Any>): String {
            return CacheAdapter.toWrapJson(v)
        }

        override fun fromJson(str: String): Map<String, Any> {
            return CacheAdapter.fromWrapJson(str)
        }
    }

    private val jsonPlaceholder
        get() = Retrofit
            .Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(JsonPlaceholder::class.java)

    interface JsonPlaceholder {
        @GET("todos/{num}")
        suspend fun fetch(@Path("num") num: Int): ResponseBody
    }
}
