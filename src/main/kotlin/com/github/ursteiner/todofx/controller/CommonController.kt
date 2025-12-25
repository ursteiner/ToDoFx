package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.AvailableLanguages
import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.service.LanguageServiceImpl
import javafx.fxml.Initializable

abstract class CommonController : Initializable {

    private val languageService = LanguageServiceImpl.getInstance(System.getProperty("user.language"))

    fun getTranslation(key: TranslationKeys): String {
        return languageService.getTranslationForKey(key)
    }

    fun setLanguage(languages: AvailableLanguages) {
        languageService.setLanguage(languages)
    }

    abstract fun initTranslations()
}