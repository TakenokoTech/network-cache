package tech.takenoko.android.ncache.domain

import tech.takenoko.android.ncache.entity.JsonPlaceholderFetchResponse

interface IApiRepository : NetworkCacheable {
    suspend fun fetch(num: Int): Result<JsonPlaceholderFetchResponse>
}
