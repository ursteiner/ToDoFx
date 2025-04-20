package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.Task
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseServiceImpl : DatabaseService {

    constructor(databasePathName: String){
        Database.connect("jdbc:h2:file:${databasePathName}", driver = "org.h2.Driver", user = "root", password = "")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Tasks)
        }
    }

    object Tasks : Table() {
        val id: Column<Int> = integer("id").autoIncrement()
        val name: Column<String> = varchar("name", length = 500)
        val date: Column<String> = varchar("date", length = 500)

        override val primaryKey = PrimaryKey(id, name = "PK_Task_ID")
    }

    override fun addTask(newTask: Task) = transaction {
        addLogger(StdOutSqlLogger)
        val taskId = Tasks.insert {
            it[name] = newTask.getNameProperty()
            it[date] = newTask.getDateProperty()
        } get Tasks.id

        newTask.setIdProperty(taskId)
    }

    override fun getTasks() : MutableList<Task> {
        val tasks = mutableListOf<Task>()

        transaction {
            addLogger(StdOutSqlLogger)
            for (task in Tasks.selectAll()) {
                //println("${task[Tasks.name]} ${task[Tasks.date]} ${task[Tasks.id]}")
                tasks.add(Task(task[Tasks.name], task[Tasks.date], task[Tasks.id]))
            }
        }
        return tasks;
    }

    override fun deleteTask(taskId: Int) : Int = transaction {
        return@transaction Tasks.deleteWhere { Tasks.id eq taskId }
    }
}