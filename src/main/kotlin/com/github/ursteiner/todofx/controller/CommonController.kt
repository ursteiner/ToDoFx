package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.database.CategoryDatabaseService
import com.github.ursteiner.todofx.database.CategoryDatabaseServiceImpl
import com.github.ursteiner.todofx.database.TaskDatabaseServiceImpl
import com.github.ursteiner.todofx.service.LanguageServiceImpl
import com.github.ursteiner.todofx.database.TaskDatabaseService
import javafx.fxml.Initializable

abstract class CommonController: Initializable{

    private val languageService = LanguageServiceImpl.getInstance(System.getProperty("user.language"))
    private val taskDatabaseService = TaskDatabaseServiceImpl.getInstance()
    private val categoryDatabaseService = CategoryDatabaseServiceImpl.getInstance()

    fun getTranslation(key: TranslationKeys): String{
        return languageService.getTranslationForKey(key)
    }

    fun getTaskDatabase(): TaskDatabaseService {
        return taskDatabaseService
    }

    fun getCategoryDatabase(): CategoryDatabaseService {
        return categoryDatabaseService
    }
}