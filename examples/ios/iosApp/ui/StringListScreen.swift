import SwiftUI
import Seahorse

struct StringListScreen: View {
    @StateObject private var viewModel = StringListViewModel(
        AppGraph.shared.seahorse,
        AppGraph.shared.stringKeys
    )

    var body: some View {
        ScrollView {
            HStack {
                Button("Swap \(viewModel.languageId)") {
                    var nextLanguage = LanguageKt.LanguageBengali
                    if nextLanguage == viewModel.languageId {
                        nextLanguage = LanguageKt.LanguageEnglish
                    }
                    viewModel.changeLanguage(nextLanguage)
                }
                .padding()
                .border(Color.blue, width: 4)
                Button("Fetch \(viewModel.languageId)") {
                    viewModel.fetchStrings(viewModel.languageId)
                }
                .padding()
                .border(Color.blue, width: 4)
                Button("Clear \(viewModel.languageId)") {
                    viewModel.clearStore(viewModel.languageId)
                }
                .padding()
                .border(Color.blue, width: 4)
            }
            LazyVStack {
                ForEach(viewModel.stringKeys, id: \.self) { key in
                    VStack {
                        Text(key)
                        if key == "platform" {
                            Text(viewModel.seahorse.getString(key, args: ["iOS"]))
                        } else if key == "sentence_structure" {
                            Text(viewModel.seahorse.getString(key, args: ["Seahorse", "gives", "strings"]))
                        } else if key == "percent" {
                            Text(viewModel.seahorse.getString(key, args: ["42"]))
                        } else {
                            Text(viewModel.seahorse.getString(key))
                        }
                    }
                    Divider()
                }
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        StringListScreen()
    }
}
