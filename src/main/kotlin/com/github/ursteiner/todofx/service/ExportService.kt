package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.Task

interface ExportService {
    fun exportTasks(tasks : MutableList<Task>)
}