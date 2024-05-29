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
            "platform",
            "sentence_structure",
            "foundation",
            "dune",
            "three_body_problem",
        ]
        self.stringKeys = stringKeys
        self.seahorse = Seahorse { b in
            b.fallbackSource = NSLocalizedFallbackSource()
            b.localStore = NativeSqliteLocalStore()
            b.networkSource = DarwinKtorNetworkSource(getUrlForLanguageId: { languageId in
                "https://www.bidyut.com/tech/seahorse/sample/\(languageId.lowercased()).json"
            })
        }
    }
}
