import SwiftUI

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            NavigationView {
                StringListScreen()
                    .navigationTitle("Seahorse Example")
            }
        }
    }
}
