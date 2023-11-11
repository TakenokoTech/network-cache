package tech.takenoko.android.ncache.repository

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tech.takenoko.android.ncache.domain.ExpiredException
import tech.takenoko.android.ncache.domain.FileNotFoundException
import tech.takenoko.android.ncache.domain.NetworkCacheable
import tech.takenoko.android.ncache.entity.CacheAdapter
import tech.takenoko.android.ncache.entity.NetworkCacheResult
import tech.takenoko.android.ncache.entity.NetworkCacheResult.Cached
import tech.takenoko.android.ncache.entity.NetworkCacheResult.Fetched
import tech.takenoko.android.ncache.utils.TimeHelper
import tech.takenoko.android.ncache.utils.toMD5
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.TimeUnit


internal interface NetworkCache : NetworkCacheable {
    val context: Context

    suspend fun <T> getCachedOrFetch(
        key: String,
        adapter: CacheAdapter<T>,
        block: suspend () -> T
    ): NetworkCacheResult<T> {
        return load(key, adapter).fold(
            onSuccess = { Cached(it) },
            onFailure = { Fetched(block().also { save(key, it, adapter) }) }
        )
    }

    suspend fun <T> load(key: String, adapter: CacheAdapter<T>) = runCatching {
        fun File.isExpired(): Boolean = TimeHelper.now() - lastModified() > CACHE_DURATION
        lock.withLock {
            val (file, encryptedFile) = getEncryptedFile(key)
            if (!file.exists()) throw FileNotFoundException()
            if (file.isExpired()) throw ExpiredException()
            val json = encryptedFile.openFileInput().use { fis ->
                fis.bufferedReader().use { it.readText() }
            }
            adapter.fromJson(json)
        }
    }

    suspend fun <T> save(key: String, value: T, adapter: CacheAdapter<T>) = runCatching {
        lock.withLock {
            val (file, encryptedFile) = getEncryptedFile(key)
            if (file.exists()) file.delete()
            encryptedFile.openFileOutput().use { fos ->
                fos.write(adapter.toJson(value).toByteArray(UTF_8))
                fos.flush()
            }
        }
    }

    private fun getEncryptedFile(key: String): Pair<File, EncryptedFile> {
        val file = File(directory(context), key.toMD5())
        val masterKey = MasterKey
            .Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val encryptedFile = EncryptedFile
            .Builder(context, file, masterKey, AES256_GCM_HKDF_4KB)
            .build()
        return file to encryptedFile
    }

    companion object {
        private const val CACHE_DIR_NAME = "cache"
        private val lock = Mutex()
        private val CACHE_DURATION = TimeUnit.SECONDS.toMillis(5)
        suspend fun clean(context: Context) = NetworkCacheCleaner.clean(lock, directory(context))

        private fun directory(context: Context) = File(context.filesDir, CACHE_DIR_NAME).also {
            if (!it.exists()) it.mkdir()
        }
    }
}
