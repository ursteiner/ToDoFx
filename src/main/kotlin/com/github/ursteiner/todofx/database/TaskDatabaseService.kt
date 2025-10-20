package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.model.Task

interface TaskDatabaseService {
    fun addTask(newTask: Task, categoryNumber: Int)
    fun getResolvedTasks(): MutableList<Task>
    fun getOpenTasks(): MutableList<Task>
    fun getTasks(resolved: Boolean? = null): MutableList<Task>
    fun getSearchedTasks(search: String): MutableList<Task>
    fun deleteTask(taskId: Int): Int
    fun updateTask(task: Task, categoryNumber: Int = -1): Int
    fun getAmountOfResolvedTasks(): Long
    fun getAmountOfOpenTasks(): Long
    fun getTasksCreatedPerMonth(lastXMonths: Int): MutableMap<String, Int>
    fun getTasksResolvedPerMonth(lastXMonths: Int): MutableMap<String, Int>
    fun getTasksPerCategory(): MutableMap<String, Int>
}