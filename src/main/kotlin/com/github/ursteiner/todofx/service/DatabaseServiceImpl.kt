package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.Category
import com.github.ursteiner.todofx.model.Task
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar


class DatabaseServiceImpl : DatabaseService {
    private val logger = LoggerFactory.getLogger(DatabaseServiceImpl::class.java)

    private constructor(databasePathName: String){
        logger.info("Database connect to $databasePathName")
        Database.connect("jdbc:h2:file:${databasePathName}", driver = "org.h2.Driver", user = "root", password = "")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Tasks)
            //SchemaUtils.create(Categories)
            SchemaUtils.createMissingTablesAndColumns(Tasks)
            SchemaUtils.createMissingTablesAndColumns(Categories)
        }
    }

    companion object {

        @Volatile
        private var instance: DatabaseServiceImpl? = null

        fun getInstance(databasePathName: String = "~/tasks") =
            instance ?: synchronized(this) {
                instance ?: DatabaseServiceImpl(databasePathName).also { instance = it }
            }
    }

    object Tasks : Table() {
        val id: Column<Int> = integer("id").autoIncrement()
        val name: Column<String> = varchar("name", length = 500)
        val date: Column<String> = varchar("date", length = 30)
        val isDone: Column<Boolean> = bool("isDone")
        val resolvedDate: Column<String?> = varchar("resolvedDate", length = 30).nullable()
        val categoryId: Column<Int?> = integer("categoryId").references(Categories.id).nullable()

        override val primaryKey = PrimaryKey(id, name = "PK_Task_ID")
    }

    object Categories : Table() {
        val id: Column<Int> = integer("id").autoIncrement()
        val name: Column<String> = varchar("name", length = 100)

        override val primaryKey = PrimaryKey(id, name = "PK_Category_ID")
    }

    override fun addTask(newTask: Task, categoryNumber: Int?) = transaction {
        logger.info("Task added: ${newTask.getNameProperty()}")
        addLogger(StdOutSqlLogger)
        val taskId = Tasks.insert {
            it[name] = newTask.getNameProperty()
            it[date] = newTask.getDateProperty()
            it[isDone] = newTask.getIsDoneProperty()
            if(categoryNumber != null){
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
                tasks.add(Task(it[Tasks.name], it[Tasks.date], it[Categories.name],it[Tasks.id], it[Tasks.isDone]))
            }
        }
        return tasks
    }

    override fun getSearchedTasks(search: String): MutableList<Task> {
        logger.info("Search tasks: $search")
        val tasks = mutableListOf<Task>()

        transaction {
            addLogger(StdOutSqlLogger)
            val databaseTasks = (Tasks leftJoin Categories).selectAll().where { LowerCase(Tasks.name) like search }

            databaseTasks.forEach {
                tasks.add(Task(it[Tasks.name], it[Tasks.date], it[Categories.name], it[Tasks.id], it[Tasks.isDone]))
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
                it[resolvedDate] = task.getResolvedDate()
                if(categoryNumber >=0 ){
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
                .where {yearMonth greaterEq getYearMonth(lastXMonths)}
                .forEach {
                    resultMap.put(it[yearMonth], it[yearMonth.count()].toInt())
                }
        }

        return resultMap
    }

    override fun getCategories(): MutableList<Category> {
        logger.info("Get Categories")
        val categories = mutableListOf<Category>()

        transaction {
            addLogger(StdOutSqlLogger)
            val databaseCategories = Categories.selectAll()

            databaseCategories.forEach {
                categories.add(Category(it[Categories.name], it[Categories.id]))
            }
        }
        return categories
    }

    override fun addCategory(category: Category) = transaction {
        logger.info("Category added: ${category.name}")
        addLogger(StdOutSqlLogger)
        val categoryId = Categories.insert {
            it[name] = category.name
        } get Categories.id

        category.id = categoryId
    }

    override fun deleteCategory(categoryId: Int): Int {
        logger.info("Delete category: $categoryId")
        var deletedCategories = 0

        transaction {
            addLogger(StdOutSqlLogger)
            deletedCategories = Categories.deleteWhere { Categories.id eq categoryId }
        }
        return deletedCategories
    }

    private fun getYearMonth(beforeXMonths: Int): String{
        val c: Calendar = GregorianCalendar()
        c.setTime(Date())
        val sdf = SimpleDateFormat("yyyy-MM")
        c.add(Calendar.MONTH, -beforeXMonths)

        return sdf.format(c.getTime())
    }
}