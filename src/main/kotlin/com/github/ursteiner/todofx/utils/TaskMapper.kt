package com.github.ursteiner.todofx.utils

import com.github.ursteiner.todofx.model.FXTask
import com.github.ursteiner.todofx.model.Task

object TaskMapper {

    fun mapFxTaskToTask(fxTask: FXTask): Task = Task(
            fxTask.getNameProperty(),
            fxTask.getDateProperty(),
            fxTask.getIdProperty(),
            fxTask.getIsDoneProperty(),
            fxTask.getResolvedDateProperty(),
            fxTask.getCategoryProperty()
    )

    fun mapTaskToFxTask(task: Task): FXTask = FXTask(
            task.name,
            task.date,
            task.id,
            task.isDone,
            task.resolvedDate,
            task.category
    )

    fun mapTasksToFxTasks(tasks: List<Task>): List<FXTask> =
        tasks.map { mapTaskToFxTask(it) }

}