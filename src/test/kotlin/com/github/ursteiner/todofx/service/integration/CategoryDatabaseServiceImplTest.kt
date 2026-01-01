package com.github.ursteiner.todofx.service.integration

import com.github.ursteiner.todofx.database.CategoryRepositoryImpl
import com.github.ursteiner.todofx.database.DatabaseProvider
import com.github.ursteiner.todofx.model.Category
import com.github.ursteiner.todofx.model.DbConnection
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class CategoryDatabaseServiceImplTest {
    private val logger = LoggerFactory.getLogger(CategoryDatabaseServiceImplTest::class.java)
    private val testCandidate = CategoryRepositoryImpl()

    @BeforeEach
    fun cleanupTestDatabase() {
        testCandidate.getCategories().forEach {
            testCandidate.deleteCategory(it.id)
            logger.info("Deleted category: ${it.id}")
        }
    }

    @Test
    fun testAddCategory() {
        val testCategory1 = Category("work", 0)
        testCandidate.addCategory(testCategory1)

        val testCategory2 = Category("private", 0)
        testCandidate.addCategory(testCategory2)

        val categories = testCandidate.getCategories()
        assertEquals(testCategory2.name, categories[0].name, "The category name should match")
        assertEquals(
            testCategory1.name,
            categories[1].name,
            "Category work should be returned as second entry because of sorting"
        )
    }

    @Test
    fun testGetCategories() {
        val amountOfCategories = testCandidate.getCategories()
        assertEquals(0, amountOfCategories.size, "There should be 0 categories")
    }

    @Test
    fun testDeleteCategory() {
        val testCategory = Category("private", 0)
        testCandidate.addCategory(testCategory)
        val deletedCategories = testCandidate.deleteCategory(testCategory.id)

        assertEquals(1, deletedCategories, "1 Category should have been deleted")
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setupDatabaseConnection() {
            val databaseTestConnection = DbConnection("jdbc:h2:mem:testTasks;DB_CLOSE_DELAY=-1;", "org.h2.Driver", "root", "")
            DatabaseProvider.connect(databaseTestConnection)
        }
    }
}