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
            }
            LazyVStack {
                ForEach(viewModel.stringKeys, id: \.self) { key in
                    Text(key)
                    let str = viewModel.seahorse.getString(key: key)
                    Text(str)
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
