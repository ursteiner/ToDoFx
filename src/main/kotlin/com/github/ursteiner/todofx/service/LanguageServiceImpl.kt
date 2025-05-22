package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.constants.AvailableLanguages
import com.github.ursteiner.todofx.constants.TranslationKeys
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

class LanguageServiceImpl : LanguageService {

    private lateinit var currentLanguage: AvailableLanguages
    private lateinit var translations: JsonObject

    private constructor(selectedLanguage: AvailableLanguages){
        setLanguage(selectedLanguage)
    }

    companion object {

        @Volatile
        private var instance: LanguageServiceImpl? = null

        fun getInstance(language: String) =
            instance ?: synchronized(this) {
                instance ?: when(language){
                    "de" -> LanguageServiceImpl(AvailableLanguages.GERMAN)
                    else -> LanguageServiceImpl(AvailableLanguages.ENGLISH)
                }.also { instance = it }
            }
    }

    override fun getTranslationForKey(key: TranslationKeys): String {
        return translations[key.name]?.jsonPrimitive?.content ?: key.name
    }

    override fun getAmountOfTranslations(): Int {
        return translations.size
    }

    override fun setLanguage(selectedLanguage: AvailableLanguages) {
        currentLanguage = selectedLanguage
        val translationFileContent = object {}.javaClass.getResourceAsStream("${selectedLanguage.language}.json")?.bufferedReader()?.readText() ?: ""
        translations = Json.decodeFromString(translationFileContent)
    }
}
