package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.utils.DateUtils
import org.jetbrains.exposed.v1.core.LowerCase
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.core.eq;
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


class TaskDatabaseServiceImpl: TaskDatabaseService {
    private val logger = LoggerFactory.getLogger(TaskDatabaseServiceImpl::class.java)

    private constructor(databasePathName: String){
        logger.info("Set database connection to $databasePathName")
        Database.connect("jdbc:h2:file:${databasePathName}", driver = "org.h2.Driver", user = "root", password = "")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Tasks)
            //SchemaUtils.createMissingTablesAndColumns(Tasks)
        }
    }

    companion object {

        @Volatile
        private var instance: TaskDatabaseServiceImpl? = null

        fun getInstance(databasePathName: String) =
            instance ?: synchronized(this) {
                instance ?: TaskDatabaseServiceImpl(databasePathName).also { instance = it }
            }
    }

    override fun addTask(newTask: Task, categoryNumber: Int) = transaction {
        logger.info("Task added: ${newTask.getNameProperty()}")
        addLogger(StdOutSqlLogger)
        val taskId = Tasks.insert {
            it[name] = newTask.getNameProperty()
            it[date] = newTask.getDateProperty()
            it[isDone] = newTask.getIsDoneProperty()
            if(categoryNumber > 0){
                it[categoryId] = categoryNumber
            }
        } get Tasks.id

        newTask.setIdProperty(taskId)
    }

    override fun getResolvedTasks(): MutableList<Task> {
        return getTasks(true)
    }

    override fun getOpenTasks(): MutableList<Task> {
        return getTasks(false)
    }

    override fun getTasks(resolved: Boolean?): MutableList<Task>{
        logger.info("Get Tasks resolved: $resolved")
        val tasks = mutableListOf<Task>()

        transaction {
            addLogger(StdOutSqlLogger)
            val databaseTasks = when(resolved){
                null -> (Tasks leftJoin Categories).selectAll()
                else -> (Tasks leftJoin Categories).selectAll().where { Tasks.isDone eq resolved }
            }
            databaseTasks.forEach {
                tasks.add(Task(it[Tasks.name], it[Tasks.date], it[Categories.name],it[Tasks.id], it[Tasks.isDone], it[Tasks.resolvedDate] ?: ""))
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
                LowerCase(Tasks.name) like search or (  LowerCase( Categories.name) like search) }

            databaseTasks.forEach {
                tasks.add(Task(it[Tasks.name], it[Tasks.date], it[Categories.name], it[Tasks.id], it[Tasks.isDone], it[Tasks.resolvedDate] ?: ""))
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
        logger.info("Update Task: ${task.getIdProperty()}")
        var updatedTasks = 0

        transaction {
            addLogger(StdOutSqlLogger)
            updatedTasks = Tasks.update({ Tasks.id eq task.getIdProperty() }) {
                it[isDone] = task.getIsDoneProperty()
                it[name] = task.getNameProperty()
                it[resolvedDate] = task.getResolvedDateProperty()
                if(categoryNumber >= 0){
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
            val yearMonth = Tasks.date.substring(0,7)
            Tasks.select(yearMonth, yearMonth.count())
                .groupBy(yearMonth)
                .where {yearMonth greaterEq DateUtils.getYearMonth(lastXMonths)}
                .forEach {
                    resultMap.put(it[yearMonth], it[yearMonth.count()].toInt())
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
                    resultMap.put(it[Categories.name], it[Categories.name.count()].toInt())
                }
        }

        return resultMap
    }
}