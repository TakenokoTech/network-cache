// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "lib",
    platforms: [
        .iOS(.v14),
        .macOS(.v12),
    ],
    products: [.library(name: "lib", targets: ["lib"])],
    dependencies: [
        .package(url: "https://github.com/realm/SwiftLint.git", from: "0.54.0"),
        .package(url: "https://github.com/nicklockwood/SwiftFormat", from: "0.52.9"),
    ],
    targets: [
        .target(name: "lib", plugins: [.plugin(name: "SwiftLintPlugin", package: "SwiftLint")]),
        .testTarget(name: "libTests", dependencies: ["lib"]),
    ]
)
