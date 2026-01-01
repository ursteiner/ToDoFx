package com.github.ursteiner.todofx.viewModel

import com.github.ursteiner.todofx.database.CategoryRepositoryImpl
import com.github.ursteiner.todofx.database.NaiveBayesRepositoryImpl
import com.github.ursteiner.todofx.database.SettingsRepositoryImpl
import com.github.ursteiner.todofx.database.TaskRepositoryImpl

object ViewModelProvider {
    val taskViewModel by lazy {
        TaskViewModel(TaskRepositoryImpl()).apply { loadOpenTasks() }
    }
    val categoryViewModel by lazy {
        CategoryViewModel(CategoryRepositoryImpl()).apply { loadCategories() }
    }
    val classificationViewModel by lazy {
        ClassificationViewModel(NaiveBayesRepositoryImpl()).apply { loadClassificationModel() }
    }
    val settingsViewModel by lazy {
        SettingsViewModel(SettingsRepositoryImpl()).apply { loadSettings() }
    }

}