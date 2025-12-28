package com.github.ursteiner.todofx.viewModel

import com.github.ursteiner.todofx.database.CategoryDatabaseServiceImpl
import com.github.ursteiner.todofx.database.NaiveBayesModelServiceImpl
import com.github.ursteiner.todofx.database.SettingsDatabaseServiceImpl
import com.github.ursteiner.todofx.database.TaskDatabaseServiceImpl

object ViewModelProvider {
    val taskViewModel by lazy {
        TaskViewModel(TaskDatabaseServiceImpl.getInstance()).apply { loadOpenTasks() }
    }
    val categoryViewModel by lazy {
        CategoryViewModel(CategoryDatabaseServiceImpl.getInstance()).apply { loadCategories() }
    }
    val classificationViewModel by lazy {
        ClassificationViewModel(NaiveBayesModelServiceImpl.getInstance()).apply { loadClassificationModel() }
    }
    val settingsViewModel by lazy {
        SettingsViewModel(SettingsDatabaseServiceImpl.getInstance()).apply { loadSettings() }
    }

}