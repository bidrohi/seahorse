package com.bidyut.tech.seahorse.data.realm

import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class StringEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var languageId: LanguageId = LanguageEnglish
    var key: String = ""
    var stringValue: String = ""
}
