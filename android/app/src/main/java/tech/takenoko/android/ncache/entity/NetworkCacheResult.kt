package tech.takenoko.android.ncache.entity

sealed class NetworkCacheResult<T>(open val value: T) {
    class Cached<T>(override val value: T) : NetworkCacheResult<T>(value)
    class Fetched<T>(override val value: T) : NetworkCacheResult<T>(value)
}
