public class ApiRepository: NetworkCache {
    func fetch(_ num: Int) async throws -> NetworkCacheResult<JsonPlaceholderFetchResponse> {
        let result = try await getCachedOrFetch(key: NetworkCacheKey.apiRepository) {
            JsonPlaceholderFetchResponse(userId: num, id: num)
        }
        return result
    }
}
