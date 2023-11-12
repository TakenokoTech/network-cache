enum NetworkCacheError: Error {
    case fileNotFoundException
    case expiredException
    case jsonParseException
}
