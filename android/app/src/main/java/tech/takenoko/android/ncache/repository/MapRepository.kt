package tech.takenoko.android.ncache.repository

import android.content.Context
import tech.takenoko.android.ncache.entity.CacheAdapter

class MapRepository(
    override val context: Context
) : NetworkCache {
    suspend fun load(key: String): Result<Any?> {
        return load("map", adapter).map { it[key] }
    }

    suspend fun save(map: Map<String, Any>): Result<Unit> {
        return save("map", map, adapter)
    }

    private val adapter = object : CacheAdapter<Map<String, Any>> {
        override fun toJson(v: Map<String, Any>): String {
            return CacheAdapter.toWrapJson(v)
        }

        override fun fromJson(str: String): Map<String, Any> {
            return CacheAdapter.fromWrapJson(str)
        }
    }
}
