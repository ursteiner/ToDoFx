package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.model.DbConnection
import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.utils.DateUtils
import org.jetbrains.exposed.v1.core.LowerCase
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.count
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.core.substring
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.slf4j.LoggerFactory


class TaskDatabaseServiceImpl : TaskDatabaseService {
    private val logger = LoggerFactory.getLogger(TaskDatabaseServiceImpl::class.java)

    private constructor(dbConnection: DbConnection) {
        logger.info("Set database connection to ${dbConnection.url}")
        Database.connect(dbConnection.url, dbConnection.driver, dbConnection.user, dbConnection.password)
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Tasks)
        }
    }

    companion object {

        @Volatile
        private var instance: TaskDatabaseServiceImpl? = null

        fun getInstance(dbConnection: DbConnection) =
            instance ?: synchronized(this) {
                instance ?: TaskDatabaseServiceImpl(dbConnection).also { instance = it }
            }
    }

    override fun addTask(newTask: Task, categoryNumber: Int) = transaction {
        logger.info("Task added: ${newTask.name}")
        addLogger(StdOutSqlLogger)
        val taskId = Tasks.insert {
            it[name] = newTask.name
            it[date] = newTask.date
            it[isDone] = newTask.isDone
            if (categoryNumber > 0) {
                it[categoryId] = categoryNumber
            }
        } get Tasks.id

        newTask.id = taskId
    }

    override fun getResolvedTasks(): MutableList<Task> {
        return getTasks(true)
    }

    override fun getOpenTasks(): MutableList<Task> {
        return getTasks(false)
    }

    override fun getTasks(resolved: Boolean?): MutableList<Task> {
        logger.info("Get Tasks resolved: $resolved")
        val tasks = mutableListOf<Task>()

        transaction {
            addLogger(StdOutSqlLogger)
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

    override fun getSearchedTasks(search: String): MutableList<Task> {
        logger.info("Search tasks: $search")
        val tasks = mutableListOf<Task>()

        transaction {
            addLogger(StdOutSqlLogger)
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
            addLogger(StdOutSqlLogger)
            deletedTasks = Tasks.deleteWhere { Tasks.id eq taskId }
        }
        return deletedTasks
    }

    override fun updateTask(task: Task, categoryNumber: Int): Int {
        logger.info("Update Task: ${task.id}")
        var updatedTasks = 0

        transaction {
            addLogger(StdOutSqlLogger)
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
        logger.info("Get amount of Tasks: $resolved")
        var amoundOfTasks: Long = 0
        transaction {
            addLogger(StdOutSqlLogger)
            amoundOfTasks = Tasks.selectAll().where { Tasks.isDone eq resolved }.count()
        }
        return amoundOfTasks
    }

    override fun getTasksPerMonth(lastXMonths: Int): MutableMap<String, Int> {
        logger.info("Get Tasks for last $lastXMonths month.")
        val resultMap = mutableMapOf<String, Int>()

        transaction {
            addLogger(StdOutSqlLogger)
            val yearMonth = Tasks.date.substring(0, 7)
            Tasks.select(yearMonth, yearMonth.count())
                .groupBy(yearMonth)
                .where { yearMonth greaterEq DateUtils.getYearMonth(lastXMonths) }
                .forEach {
                    resultMap[it[yearMonth]] = it[yearMonth.count()].toInt()
                }
        }

        return resultMap
    }

    override fun getTasksPerCategory(): MutableMap<String, Int> {
        logger.info("Get Tasks per category.")
        val resultMap = mutableMapOf<String, Int>()

        transaction {
            addLogger(StdOutSqlLogger)
            (Categories innerJoin Tasks).select(Categories.name, Categories.name.count())
                .groupBy(Categories.name)
                .forEach {
                    resultMap[it[Categories.name]] = it[Categories.name.count()].toInt()
                }
        }

        return resultMap
    }
}