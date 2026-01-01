package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.model.Task

interface TaskRepository {
    fun addTask(newTask: Task, categoryNumber: Int)
    fun getResolvedTasks(): List<Task>
    fun getOpenTasks(): List<Task>
    fun getTasks(resolved: Boolean? = null): List<Task>
    fun getSearchedTasks(search: String): List<Task>
    fun deleteTask(taskId: Int): Int
    fun updateTask(task: Task, categoryNumber: Int = -1): Int
    fun getAmountOfResolvedTasks(): Long
    fun getAmountOfOpenTasks(): Long
    fun getTasksCreatedPerMonth(lastXMonths: Int): Map<String, Int>
    fun getTasksResolvedPerMonth(lastXMonths: Int): Map<String, Int>
    fun getTasksPerCategory(): Map<String, Int>
}