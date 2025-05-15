package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.Category

interface CategoriesDatabaseService {
    fun getCategories(): MutableList<Category>
    fun addCategory(category: Category)
    fun deleteCategory(categoryId: Int): Int
}