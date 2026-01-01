package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.utils.DateUtils
import org.jetbrains.exposed.v1.core.LowerCase
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.count
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.core.substring
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.slf4j.LoggerFactory


class TaskRepositoryImpl : TaskRepository {
    private val logger = LoggerFactory.getLogger(TaskRepositoryImpl::class.java)

    init {
        transaction {
            SchemaUtils.create(Tasks)
        }
    }

    override fun addTask(newTask: Task, categoryNumber: Int) = transaction {
        logger.info("Task added: ${newTask.name}")
        val taskId = Tasks.insert {
            it[name] = newTask.name
            it[date] = newTask.date
            it[isDone] = newTask.isDone
            //is a category set for the task
            if (categoryNumber > 0) {
                it[categoryId] = categoryNumber
            }
        } get Tasks.id

        newTask.id = taskId
    }

    override fun getResolvedTasks(): List<Task> {
        return getTasks(true)
    }

    override fun getOpenTasks(): List<Task> {
        return getTasks(false)
    }

    override fun getTasks(resolved: Boolean?): List<Task> {
        logger.info("Get Tasks resolved: $resolved")
        val tasks = mutableListOf<Task>()

        transaction {
            val databaseTasks = when (resolved) {
                null -> (Tasks leftJoin Categories).selectAll()
                else -> (Tasks leftJoin Categories).selectAll().where { Tasks.isDone eq resolved }
            }
            databaseTasks.forEach {
                tasks.add(
                    Task(
                        it[Tasks.name],
                        it[Tasks.date],
                        it[Tasks.id],
                        it[Tasks.isDone],
                        it[Tasks.resolvedDate] ?: "",
                        it[Categories.name]
                    )
                )
            }
        }
        return tasks
    }

    override fun getSearchedTasks(search: String): List<Task> {
        logger.info("Search tasks: $search")
        val tasks = mutableListOf<Task>()

        transaction {
            val databaseTasks = (Tasks leftJoin Categories).selectAll().where {
                LowerCase(Tasks.name) like search or (LowerCase(Categories.name) like search)
            }

            databaseTasks.forEach {
                tasks.add(
                    Task(
                        it[Tasks.name],
                        it[Tasks.date],
                        it[Tasks.id],
                        it[Tasks.isDone],
                        it[Tasks.resolvedDate] ?: "",
                        it[Categories.name]
                    )
                )
            }
        }
        return tasks
    }

    override fun deleteTask(taskId: Int): Int {
        logger.info("Delete Task: $taskId")
        var deletedTasks = 0

        transaction {
            deletedTasks = Tasks.deleteWhere { Tasks.id eq taskId }
        }
        return deletedTasks
    }

    override fun updateTask(task: Task, categoryNumber: Int): Int {
        logger.info("Update Task: ${task.id}")
        var updatedTasks = 0

        transaction {
            updatedTasks = Tasks.update({ Tasks.id eq task.id }) {
                it[isDone] = task.isDone
                it[name] = task.name
                it[resolvedDate] = task.resolvedDate
                if (categoryNumber >= 0) {
                    it[categoryId] = categoryNumber
                }
            }
        }
        return updatedTasks
    }

    override fun getAmountOfResolvedTasks(): Long {
        return getAmountOfTasks(true)
    }

    override fun getAmountOfOpenTasks(): Long {
        return getAmountOfTasks(false)
    }

    private fun getAmountOfTasks(resolved: Boolean): Long {
        logger.info("Get amount of Tasks (resolved = $resolved)")
        var amoundOfTasks: Long = 0
        transaction {
            amoundOfTasks = Tasks.selectAll().where { Tasks.isDone eq resolved }.count()
        }
        return amoundOfTasks
    }

    @Suppress("UNCHECKED_CAST")
    override fun getTasksCreatedPerMonth(lastXMonths: Int): Map<String, Int> {
        logger.info("Get Tasks created in the last $lastXMonths month.")
        return getTasksCreatedOrResolvedPerMonth(lastXMonths, Tasks.date as Column<String?>)
    }

    override fun getTasksResolvedPerMonth(lastXMonths: Int): Map<String, Int> {
        logger.info("Get Tasks resolved in the last $lastXMonths month.")
        return getTasksCreatedOrResolvedPerMonth(lastXMonths, Tasks.resolvedDate)
    }

    private fun getTasksCreatedOrResolvedPerMonth(lastXMonths: Int, dateField: Column<String?>): Map<String, Int> {
        val resultMap = mutableMapOf<String, Int>()

        transaction {
            val yearMonth = dateField.substring(0, 7)
            Tasks.select(yearMonth, yearMonth.count())
                .groupBy(yearMonth)
                .where { yearMonth neq "" and (yearMonth greaterEq DateUtils.getYearMonth(lastXMonths)) }
                .forEach {
                    resultMap[it[yearMonth]] = it[yearMonth.count()].toInt()
                }
        }

        return resultMap
    }

    override fun getTasksPerCategory(): Map<String, Int> {
        logger.info("Get Tasks per category.")
        val resultMap = mutableMapOf<String, Int>()

        transaction {
            (Categories innerJoin Tasks).select(Categories.name, Categories.name.count())
                .groupBy(Categories.name)
                .forEach {
                    resultMap[it[Categories.name]] = it[Categories.name.count()].toInt()
                }
        }

        return resultMap
    }
}