package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.AvailableLanguages
import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.database.CategoryDatabaseService
import com.github.ursteiner.todofx.database.CategoryDatabaseServiceImpl
import com.github.ursteiner.todofx.database.TaskDatabaseServiceImpl
import com.github.ursteiner.todofx.service.LanguageServiceImpl
import com.github.ursteiner.todofx.database.TaskDatabaseService
import javafx.fxml.Initializable

abstract class CommonController : Initializable {

    val defaultDataBasePathName = "~/tasks"

    private val languageService = LanguageServiceImpl.getInstance(System.getProperty("user.language"))
    private val taskDatabaseService = TaskDatabaseServiceImpl.getInstance(defaultDataBasePathName)
    private val categoryDatabaseService = CategoryDatabaseServiceImpl.getInstance(defaultDataBasePathName)

    fun getTranslation(key: TranslationKeys): String {
        return languageService.getTranslationForKey(key)
    }

    fun setLanguage(languages: AvailableLanguages) {
        languageService.setLanguage(languages)
    }

    fun getTaskDatabase(): TaskDatabaseService {
        return taskDatabaseService
    }

    fun getCategoryDatabase(): CategoryDatabaseService {
        return categoryDatabaseService
    }

    abstract fun initTranslations()
}