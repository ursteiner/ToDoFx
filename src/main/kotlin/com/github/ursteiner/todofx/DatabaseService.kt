package com.github.ursteiner.todofx

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseService {

    constructor(){
        Database.connect("jdbc:h2:file:~/tasks", driver = "org.h2.Driver", user = "root", password = "")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Tasks)
        }
    }

    object Tasks : Table() {
        val id: Column<Int> = integer("id").autoIncrement()
        val name: Column<String> = varchar("name", length = 500)
        val date: Column<String> = varchar("date", length = 500)

        override val primaryKey = PrimaryKey(id, name = "PK_Task_ID") // name is optional here
    }

    fun addTask(newTask: Task){
        transaction {
            Tasks.insert {
                it[name] = newTask.getNameProperty()
                it[date] = newTask.getDateProperty()
            }
        }
    }

    fun getTasks() : MutableList<Task> {

        val tasks = mutableListOf<Task>()
        transaction {
            for(task in Tasks.selectAll()){
                tasks.add(Task(task[Tasks.name], task[Tasks.date]))
            }
        }

        return tasks;

    }

}