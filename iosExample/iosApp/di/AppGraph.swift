//
//  AppGraph.swift
//  iosApp
//
//  Created by Saud Khan on 2023-09-23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import Seahorse

class AppGraph {
    static let shared: AppGraph = AppGraph()

    let stringKeys: [String]
    let seahorse: Seahorse

    private init() {
        let stringKeys = [
            "hello",
            "world",
            "foundation",
            "dune",
            "three_body_problem",
        ]
        self.stringKeys = stringKeys
        self.seahorse = Seahorse { b in
            b.fallbackSource = NSLocalizedFallbackSource(rootBundle: Bundle.main, tableName: nil)
            let sourceSink = MapLocalSourceSink()
            b.localSource = sourceSink
            b.localSink = sourceSink
            b.networkSource = MapNetworkSource(
                stringMapByLanguage: [
                    LanguageKt.LanguageEnglish: stringKeys.reduce(into: [String: String]()) { $0[$1] = "network \($1)" },
                    LanguageKt.LanguageBengali: stringKeys.reduce(into: [String: String]()) { $0[$1] = "অন্তর্জাল \($1)" },
                ]
            )
        }
    }
}
