public class Lib {
    private final let SLEEP = UInt64(1000 * 1000 * 1000)

    public init() {
        Task {
            var count = 0
            repeat {
                count += 1
                await print(Result.async { try await ApiRepository().fetch(1) })
                try await Task.sleep(nanoseconds: SLEEP)
            } while count < 10
        }
    }
}
