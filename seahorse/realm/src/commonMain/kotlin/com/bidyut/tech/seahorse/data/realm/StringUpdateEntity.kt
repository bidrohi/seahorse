package com.bidyut.tech.seahorse.data.realm

import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class StringUpdateEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var languageId: LanguageId = LanguageEnglish
    var updatedAt: RealmInstant = RealmInstant.MIN
}
