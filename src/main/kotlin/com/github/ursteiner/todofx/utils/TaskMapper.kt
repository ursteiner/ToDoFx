package com.github.ursteiner.todofx.utils

import com.github.ursteiner.todofx.model.FXTask
import com.github.ursteiner.todofx.model.Task

object TaskMapper {

    fun mapFxTaskToTask(fxTask: FXTask): Task {
        return Task(
            fxTask.getNameProperty(),
            fxTask.getDateProperty(),
            fxTask.getIdProperty(),
            fxTask.getIsDoneProperty(),
            fxTask.getResolvedDateProperty(),
            fxTask.getCategoryProperty()
        )
    }

    fun mapTaskToFxTask(task: Task): FXTask {
        return FXTask(
            task.name,
            task.date,
            task.id,
            task.isDone,
            task.resolvedDate,
            task.category
        )
    }

    fun mapTasksToFxTasks(tasks: MutableList<Task>): MutableList<FXTask> {
        val fxTasks = mutableListOf<FXTask>()

        tasks.forEach {
            fxTasks.add(mapTaskToFxTask(it))
        }

        return fxTasks
    }
}