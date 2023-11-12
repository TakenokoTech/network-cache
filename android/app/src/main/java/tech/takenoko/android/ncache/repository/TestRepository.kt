package tech.takenoko.android.ncache.repository

import android.content.Context
import tech.takenoko.android.ncache.domain.ITestRepository
import tech.takenoko.android.ncache.entity.NetworkCacheKey
import tech.takenoko.android.ncache.entity.TestResponse


class TestRepository(
    override val context: Context
) : ITestRepository, NetworkCache {
    override suspend fun load(key: String): Result<TestResponse> {
        return load(NetworkCacheKey.TestRepository(key))
    }

    override suspend fun save(key: String, value: TestResponse): Result<Unit> {
        return save(NetworkCacheKey.TestRepository(key), value)
    }
}


