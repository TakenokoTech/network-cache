package tech.takenoko.android.ncache.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File

internal object NetworkCacheCleaner {
    suspend fun clean(lock: Mutex, directory: File) = runCatching {
        lock.withLock {
            directory.listFiles().orEmpty().forEach {
                it.deleteRecursively()
            }
        }
    }
}
