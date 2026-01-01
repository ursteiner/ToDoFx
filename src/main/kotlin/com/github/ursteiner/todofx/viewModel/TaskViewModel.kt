package com.github.ursteiner.todofx.viewModel

import com.github.ursteiner.todofx.database.TaskRepository
import com.github.ursteiner.todofx.model.FXTask
import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.utils.TaskMapper
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class TaskViewModel(
    private val taskDb: TaskRepository
) {
    private val isDoneTextIcon = "✔"
    val tasks: ObservableList<FXTask> = FXCollections.observableArrayList()

    private fun markDoneTasks(){
        tasks.filter { it.getIsDoneProperty() }
            .forEach { it.setIsDoneIconProperty(isDoneTextIcon) }
    }

    fun loadTasks() {
        tasks.setAll(TaskMapper.mapTasksToFxTasks(taskDb.getTasks()))
        markDoneTasks()
    }

    fun loadOpenTasks() {
        tasks.setAll(TaskMapper.mapTasksToFxTasks(taskDb.getOpenTasks()))
    }

    fun addTask(task: FXTask, categoryId: Int) {
        taskDb.addTask(TaskMapper.mapFxTaskToTask(task), categoryId)
        loadTasks()
    }

    fun updateTask(task: FXTask){
        updateTask(task, -1)
    }

    fun updateTask(task: FXTask, categoryId: Int) {
        taskDb.updateTask(TaskMapper.mapFxTaskToTask(task), categoryId)
    }

    fun deleteTask(task: FXTask) {
        taskDb.deleteTask(task.getIdProperty())
        loadTasks()
    }

    fun searchTasks(query: String) {
        tasks.setAll(taskDb.getSearchedTasks("%${query.lowercase()}%").map { TaskMapper.mapTaskToFxTask(it) })
        markDoneTasks()
    }

    fun getTaskList(): MutableList<Task> {
        return taskDb.getTasks().toMutableList()
    }

    fun getTasksVisibleOfTotalText(taskTranslation: String): String {
        return "${tasks.size}" +
                " / " +
                "${taskDb.getAmountOfOpenTasks() + taskDb.getAmountOfResolvedTasks()}" +
                " $taskTranslation"
    }

    fun getAmountOfOpenTasks(): Double {
        return taskDb.getAmountOfOpenTasks().toDouble()
    }

    fun getAmountOfResolvedTasks(): Double {
        return taskDb.getAmountOfResolvedTasks().toDouble()
    }

    fun getTasksPerCategory(): Map<String, Int> {
        return taskDb.getTasksPerCategory()
    }

    fun getTasksCreatedPerMonth(lastXMonths: Int): Map<String, Int> {
        return taskDb.getTasksCreatedPerMonth(lastXMonths)
    }

    fun getTasksResolvedPerMonth(lastXMonths: Int): Map<String, Int> {
        return taskDb.getTasksResolvedPerMonth(lastXMonths)
    }
}