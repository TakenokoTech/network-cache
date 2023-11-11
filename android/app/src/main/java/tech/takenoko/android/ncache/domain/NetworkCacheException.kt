package tech.takenoko.android.ncache.domain

sealed class NetworkCacheException : Exception()
class ExpiredException : NetworkCacheException()
class FileNotFoundException : NetworkCacheException()
class JsonParseException : NetworkCacheException()
