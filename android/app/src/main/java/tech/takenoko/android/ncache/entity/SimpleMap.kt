package tech.takenoko.android.ncache.entity

import tech.takenoko.android.ncache.domain.CacheAdapter

typealias SimpleMap = Map<String, Any>

object SimpleMapAdapter : CacheAdapter<SimpleMap> {
    override fun toJson(v: SimpleMap) = CacheAdapter.toWrapJson(v)
    override fun fromJson(str: String) = CacheAdapter.fromWrapJson<SimpleMap>(str)
}
