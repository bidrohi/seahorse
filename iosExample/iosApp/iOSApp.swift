import SwiftUI
import Seahorse
import BackgroundTasks

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    var body: some Scene {
        WindowGroup {
            NavigationView {
                StringListScreen()
                    .navigationTitle("Seahorse Example")
            }
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        if #available(iOS 13, *) {
            AppGraph.shared.seahorse.registerBackgroundTask(languages: [LanguageKt.LanguageEnglish, LanguageKt.LanguageBengali])
        } else {
            UIApplication.shared.setMinimumBackgroundFetchInterval(
                max(UIApplication.backgroundFetchIntervalMinimum, AppGraph.shared.seahorse.getCacheInterval())
            )
        }
        return true
    }

    func application(_ application: UIApplication, performFetchWithCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        if AppGraph.shared.seahorse.refreshStrings(languages: [LanguageKt.LanguageEnglish, LanguageKt.LanguageBengali]) {
            completionHandler(.newData)
        } else {
            completionHandler(.failed)
        }
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        if #available(iOS 13, *) {
            AppGraph.shared.seahorse.schedule()
        }
    }
}
