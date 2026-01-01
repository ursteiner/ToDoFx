package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.model.Category
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.slf4j.LoggerFactory

class CategoryRepositoryImpl : CategoryRepository {

    private val logger = LoggerFactory.getLogger(CategoryRepositoryImpl::class.java)

    init {
        transaction {
            SchemaUtils.create(Categories)
        }
    }

    override fun getCategories(): List<Category> = transaction {
        logger.info("Get Categories")

        Categories.selectAll().orderBy(Categories.name, SortOrder.ASC)
            .map { Category(it[Categories.name], it[Categories.id]) }
    }

    override fun addCategory(category: Category) = transaction {
        logger.info("Category added: ${category.name}")

        val categoryId = Categories.insert {
            it[name] = category.name
        } get Categories.id

        category.id = categoryId
    }

    override fun deleteCategory(categoryId: Int): Int {
        logger.info("Delete category: $categoryId")
        var deletedCategories = 0

        transaction {
            deletedCategories = Categories.deleteWhere { Categories.id eq categoryId }
        }
        return deletedCategories
    }

    override fun updateCategory(category: Category): Int {
        logger.info("Update Category: ${category.id}")
        var updatedCategories = 0

        transaction {
            updatedCategories = Categories.update({ Categories.id eq category.id }) {
                it[name] = category.name
            }
        }
        return updatedCategories
    }
}