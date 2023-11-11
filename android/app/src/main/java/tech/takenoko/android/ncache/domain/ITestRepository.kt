package tech.takenoko.android.ncache.domain

import tech.takenoko.android.ncache.entity.TestResponse

interface ITestRepository : NetworkCacheable {
    suspend fun load(key: String): Result<TestResponse>
    suspend fun save(key: String, value: TestResponse): Result<Unit>
}
