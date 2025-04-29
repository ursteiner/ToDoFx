package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.constants.AvailableLanguages
import com.github.ursteiner.todofx.constants.TranslationKeys
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

class LanguageServiceImpl : LanguageService {

    private val currentLanguage: AvailableLanguages
    private var translations: JsonObject

    constructor(selectedLanguage: AvailableLanguages){
        currentLanguage = selectedLanguage
        val translationFileContent = object {}.javaClass.getResourceAsStream("${selectedLanguage.language}.json")?.bufferedReader()?.readText() ?: ""
        translations = Json.decodeFromString(translationFileContent)
    }

    override fun getTranslationForKey(key: TranslationKeys): String {
        return translations[key.name]?.jsonPrimitive?.content ?: key.name
    }

    override fun getAmountOfTranslations(): Int {
        return translations.size
    }
}
