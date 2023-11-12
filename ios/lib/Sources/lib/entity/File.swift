import Foundation

struct File {
    let url: URL

    static let fileManager = FileManager.default

    func read() throws -> String { try String(contentsOf: url, encoding: .utf8) }
    func save(_ json: String) throws { try json.write(to: url, atomically: true, encoding: .utf8) }
    func delete() { try? File.fileManager.removeItem(at: url) }
    func exists() -> Bool { File.fileManager.fileExists(atPath: url.path) }

    func isExpired() -> Bool {
        let attributes = try? FileManager.default.attributesOfItem(atPath: url.path)
        let modificationDate = attributes?[.modificationDate] as? Date
        guard let modificationDate = modificationDate else { return true }
        return Date().timeIntervalSince(modificationDate) > 5 // 5mils
    }
}
