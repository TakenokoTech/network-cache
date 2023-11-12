enum NetworkCacheResult<T> {
    case cached(_ value: T)
    case fetched(_ value: T)

    var value: T {
        switch self {
        case let .cached(value): return value
        case let .fetched(value): return value
        }
    }
}
