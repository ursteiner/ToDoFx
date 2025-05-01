package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.Task
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.LowerCase
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class DatabaseServiceImpl : DatabaseService {

    private constructor(databasePathName: String){
        Database.connect("jdbc:h2:file:${databasePathName}", driver = "org.h2.Driver", user = "root", password = "")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Tasks)
        }
    }

    companion object {

        @Volatile
        private var instance: DatabaseServiceImpl? = null

        fun getInstance(databasePathName: String) =
            instance ?: synchronized(this) {
                instance ?: DatabaseServiceImpl(databasePathName).also { instance = it }
            }
    }

    object Tasks : Table() {
        val id: Column<Int> = integer("id").autoIncrement()
        val name: Column<String> = varchar("name", length = 500)
        val date: Column<String> = varchar("date", length = 500)
        var isDone : Column<Boolean> = bool("isDone")

        override val primaryKey = PrimaryKey(id, name = "PK_Task_ID")
    }

    override fun addTask(newTask: Task) = transaction {
        addLogger(StdOutSqlLogger)
        val taskId = Tasks.insert {
            it[name] = newTask.getNameProperty()
            it[date] = newTask.getDateProperty()
            it[isDone] = newTask.getIsDoneProperty()
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
        val tasks = mutableListOf<Task>()

        transaction {
            addLogger(StdOutSqlLogger)
            val databaseTasks = when(resolved){
                null -> Tasks.selectAll()
                else -> Tasks.selectAll().where { Tasks.isDone eq resolved }
            }
            databaseTasks.forEach {
                tasks.add(Task(it[Tasks.name], it[Tasks.date], it[Tasks.id], it[Tasks.isDone]))
            }
        }
        return tasks
    }

    override fun getSearchedTasks(search: String): MutableList<Task> {
        val tasks = mutableListOf<Task>()

        transaction {
            addLogger(StdOutSqlLogger)
            val databaseTasks = Tasks.selectAll().where { LowerCase(Tasks.name) like search }

            databaseTasks.forEach {
                tasks.add(Task(it[Tasks.name], it[Tasks.date], it[Tasks.id], it[Tasks.isDone]))
            }
        }
        return tasks
    }

    override fun deleteTask(taskId: Int): Int {
        var deletedTasks = 0

        transaction {
            addLogger(StdOutSqlLogger)
            deletedTasks = Tasks.deleteWhere { Tasks.id eq taskId }
        }
        return deletedTasks
    }

    override fun updateTask(task: Task): Int {
        var updatedTasks = 0

        transaction {
            addLogger(StdOutSqlLogger)
            updatedTasks = Tasks.update({ Tasks.id eq task.getIdProperty() }) {
                it[isDone] = task.getIsDoneProperty()
                it[name] = task.getNameProperty()
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
        var amoundOfTasks: Long = 0
        transaction {
            addLogger(StdOutSqlLogger)
            amoundOfTasks = Tasks.selectAll().where { Tasks.isDone eq resolved }.count()
        }
        return amoundOfTasks
    }
}