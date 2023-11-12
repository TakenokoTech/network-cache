import Foundation

protocol NetworkCache: NetworkCacheable {
    func getCachedOrFetch<T: Decodable & Encodable>(
        key: NetworkCacheKey<T>,
        block: () async -> T
    ) async throws -> NetworkCacheResult<T>
}

extension NetworkCache {
    func getCachedOrFetch<T: Decodable & Encodable>(
        key: NetworkCacheKey<T>,
        block: () async -> T
    ) async throws -> NetworkCacheResult<T> {
        if let value = try? await NetworkCacheActor.shared.load(key: key) { return .cached(value) }
        let result = await block()
        _ = try await NetworkCacheActor.shared.save(key: key, value: result)
        return .fetched(result)
    }
}

private actor NetworkCacheActor {
    static let shared = NetworkCacheActor()

    func load<T: Decodable>(key: NetworkCacheKey<T>) throws -> T {
        let file = try getEncryptedFile(key: key)
        if !file.exists() { throw NetworkCacheError.fileNotFoundException }
        if file.isExpired() { throw NetworkCacheError.expiredException }
        guard let data = try file.read().data(using: .utf8) else { throw NetworkCacheError.jsonParseException }
        return try JSONDecoder().decode(T.self, from: data)
    }

    func save<T: Encodable>(key: NetworkCacheKey<T>, value: T) throws {
        let file = try getEncryptedFile(key: key)
        if file.exists() { file.delete() }
        let data = try JSONEncoder().encode(value)
        guard let json = String(data: data, encoding: .utf8) else { throw NetworkCacheError.jsonParseException }
        try file.save(json)
    }

    private func getEncryptedFile<T>(key: NetworkCacheKey<T>) throws -> File {
        try File(url: directory().appendingPathComponent(key.md5))
    }

    private func directory() throws -> URL {
        let fileManager = FileManager.default
        let cacheDir = fileManager.urls(for: .cachesDirectory, in: .userDomainMask).first
        guard let targetDir = cacheDir?.appendingPathComponent("cache") else { fatalError() }
        try fileManager.createDirectory(atPath: targetDir.path, withIntermediateDirectories: true)
        return targetDir
    }
}
