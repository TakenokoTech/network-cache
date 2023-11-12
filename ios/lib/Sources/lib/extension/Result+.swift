extension Result<Any, Error> {
    static func async<T>(_ block: () async throws -> T) async -> Result<T, Error> {
        do {
            return try await .success(block())
        } catch {
            return .failure(error)
        }
    }
}
