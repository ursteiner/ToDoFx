package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.model.Category
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.addLogger
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory

class CategoryDatabaseServiceImpl : CategoryDatabaseService {

    private val logger = LoggerFactory.getLogger(CategoryDatabaseServiceImpl::class.java)

    private constructor(databasePathName: String){
        logger.info("Set database connection to $databasePathName")
        Database.connect("jdbc:h2:file:${databasePathName}", driver = "org.h2.Driver", user = "root", password = "")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Categories)
            //SchemaUtils.createMissingTablesAndColumns(Categories)
        }
    }

    companion object {

        @Volatile
        private var instance: CategoryDatabaseServiceImpl? = null

        fun getInstance(databasePathName: String = "~/tasks") =
            instance ?: synchronized(this) {
                instance ?: CategoryDatabaseServiceImpl(databasePathName).also { instance = it }
            }
    }

    override fun getCategories(): MutableList<Category> {
        logger.info("Get Categories")
        val categories = mutableListOf<Category>()

        transaction {
            addLogger(StdOutSqlLogger)
            val databaseCategories = Categories.selectAll().orderBy(Categories.name, SortOrder.ASC)

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
}