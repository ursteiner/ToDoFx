package com.github.ursteiner.todofx.service.integration

import com.github.ursteiner.todofx.database.CategoryDatabaseServiceImpl
import com.github.ursteiner.todofx.model.Category
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class CategoryDatabaseServiceImplTest {
    private val logger = LoggerFactory.getLogger(CategoryDatabaseServiceImplTest::class.java)
    private val testCandidate = CategoryDatabaseServiceImpl.Companion.getInstance("./testTasks")

    @BeforeEach
    fun cleanupTestDatabase() {
        testCandidate.getCategories().forEach {
            testCandidate.deleteCategory(it.id)
            logger.info("Deleted category: ${it.id}")
        }
    }

    @Test
    fun testAddCategory(){
        val testCategory1 = Category("work", 0)
        testCandidate.addCategory(testCategory1)

        val testCategory2 = Category("private", 0)
        testCandidate.addCategory(testCategory2)

        val categories = testCandidate.getCategories()
        Assertions.assertEquals(testCategory2.name, categories[0].name, "The category name should match")
        Assertions.assertEquals(
            testCategory1.name,
            categories[1].name,
            "Category work should be returned as second entry because of sorting"
        )
    }

    @Test
    fun testGetCategories(){
        val amountOfCategories = testCandidate.getCategories()
        Assertions.assertEquals(0, amountOfCategories.size, "There should be 0 categories")
    }

    @Test
    fun testDeleteCategory(){
        val testCategory = Category("private", 0)
        testCandidate.addCategory(testCategory)
        val deletedCategories = testCandidate.deleteCategory(testCategory.id)

        Assertions.assertEquals(1, deletedCategories, "1 Category should have been deleted")
    }
}