package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.constants.AvailableLanguages
import com.github.ursteiner.todofx.constants.TranslationKeys

interface LanguageService {
    fun getTranslationForKey(key: TranslationKeys): String
    fun getAmountOfTranslations(): Int
    fun setLanguage(selectedLanguage: AvailableLanguages)
}
