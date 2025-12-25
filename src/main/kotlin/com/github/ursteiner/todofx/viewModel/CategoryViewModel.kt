package com.github.ursteiner.todofx.viewModel

import com.github.ursteiner.todofx.database.CategoryDatabaseService
import com.github.ursteiner.todofx.model.Category
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class CategoryViewModel(
    private val categoryDb: CategoryDatabaseService
) {

    val categories: ObservableList<Category> = FXCollections.observableArrayList()
    val categoryNames: ObservableList<String> = FXCollections.observableArrayList()

    fun loadCategories() {
        categories.setAll(categoryDb.getCategories())
        categoryNames.setAll(categories.map { it.name })
    }

    fun addCategory(category: Category) {
        categoryDb.addCategory(category)
        loadCategories()
    }

    fun updateCategory(category: Category) {
        categoryDb.updateCategory(category)
        loadCategories()
    }

    fun deleteCategory(category: Category) {
        categoryDb.deleteCategory(category.id)
        loadCategories()
    }
}