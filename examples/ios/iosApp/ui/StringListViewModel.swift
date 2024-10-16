//
//  StringLiveViewModel.swift
//  iosApp
//
//  Created by Saud Khan on 2023-09-23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import Seahorse

class StringListViewModel: ObservableObject {
    let seahorse: Seahorse
    let stringKeys: [String]

    @Published var languageId = LanguageKt.LanguageEnglish
    @Published var lastUpdated = Date.distantPast

    init(
        _ seahorse: Seahorse,
        _ stringKeys: [String]
    ) {
        self.seahorse = seahorse
        self.stringKeys = stringKeys
    }

    func changeLanguage(
        _ languageId: String
    ) {
        self.seahorse.defaultLanguageId = languageId
        self.languageId = languageId
    }

    func fetchStrings(
        _ languageId: String
    ) {
        self.seahorse.fetchStringsAsync(languageId, forceUpdate: false) { updatedTime, error in
            if error != nil {
                // handle error
            } else {
                self.lastUpdated = updatedTime ?? Date.distantPast
            }
        }
    }

    func clearStore(
        _ languageId: String
    ) {
        if self.seahorse.clearStore([languageId]) > 0 {
            self.lastUpdated = Date.distantPast
        }
    }
}
