package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.Task

interface DatabaseService {

    fun addTask(newTask: Task)
    fun getTasks() : MutableList<Task>
    fun deleteTask(taskId: Int) : Int

}