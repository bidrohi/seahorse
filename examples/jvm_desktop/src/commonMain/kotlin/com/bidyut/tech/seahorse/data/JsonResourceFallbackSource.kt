package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.jvm_desktop.generated.resources.Res
import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
class JsonResourceFallbackSource : JsonFallbackSource() {
    override fun fetchStrings(languageId: LanguageId): Map<String, String> {
        return runBlocking(Dispatchers.IO) {
            val bytes = Res.readBytes("files/$languageId.json")
            val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
            val stringMap = json.decodeFromString<Map<String, String>>(bytes.decodeToString())
            stringMap
        }
    }
}
