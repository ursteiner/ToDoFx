package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
import com.github.ursteiner.todofx.service.LanguageServiceImpl
import javafx.fxml.Initializable

abstract class CommonController: Initializable{

    private val languageService = LanguageServiceImpl.getInstance(System.getProperty("user.language"))
    private val databaseService = DatabaseServiceImpl.getInstance()

    fun getTranslation(key: TranslationKeys): String{
        return languageService.getTranslationForKey(key)
    }

    fun getDatabase(): DatabaseServiceImpl {
        return databaseService
    }
}