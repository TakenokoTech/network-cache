package tech.takenoko.android.ncache.repository

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import tech.takenoko.android.ncache.domain.IApiRepository
import tech.takenoko.android.ncache.entity.JsonPlaceholderFetchResponse
import tech.takenoko.android.ncache.entity.JsonPlaceholderFetchResponse.Adapter
import tech.takenoko.android.ncache.entity.NetworkCacheResult.Cached
import tech.takenoko.android.ncache.utils.moshi


class ApiRepository(
    override val context: Context
) : IApiRepository, NetworkCache {
    override suspend fun fetch(num: Int) = runCatching {
        val result = getCachedOrFetch("ApiRepository${num}", Adapter) {
            jsonPlaceholder.fetch(num)
        }
        result.value.copy(isCache = result is Cached)
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
        suspend fun fetch(@Path("num") num: Int): JsonPlaceholderFetchResponse
    }
}


