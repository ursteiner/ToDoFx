package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.model.Category

interface CategoryRepository {
    fun getCategories(): List<Category>
    fun addCategory(category: Category)
    fun deleteCategory(categoryId: Int): Int
    fun updateCategory(category: Category): Int
}