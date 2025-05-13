package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.Category
import com.github.ursteiner.todofx.model.Task

interface DatabaseService {
    fun addTask(newTask: Task)
    fun getResolvedTasks(): MutableList<Task>
    fun getOpenTasks(): MutableList<Task>
    fun getTasks(resolved: Boolean? = null): MutableList<Task>
    fun getSearchedTasks(search: String): MutableList<Task>
    fun deleteTask(taskId: Int): Int
    fun updateTask(task: Task): Int
    fun getAmountOfResolvedTasks(): Long
    fun getAmountOfOpenTasks(): Long
    fun getTasksPerMonth(lastXMonths: Int): MutableMap<String, Int>

    fun getCategories(): MutableList<Category>
    fun addCategory(category: Category)
    fun deleteCategory(categoryId: Int): Int
}