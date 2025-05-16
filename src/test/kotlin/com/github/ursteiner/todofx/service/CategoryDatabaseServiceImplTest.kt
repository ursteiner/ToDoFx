package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.database.CategoryDatabaseServiceImpl
import com.github.ursteiner.todofx.model.Category
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoryDatabaseServiceImplTest {

    val testCandidate = CategoryDatabaseServiceImpl.getInstance("./testTasks")

    @BeforeEach
    fun cleanupTestDatabase() {
        testCandidate.getCategories().forEach {
            testCandidate.deleteCategory(it.id)
            println("Deleted category: ${it.id}")
        }
    }

    @Test
    fun testAddCategory(){
        val testCategory = Category("privat", 0)
        testCandidate.addCategory(testCategory)
        val categories = testCandidate.getCategories()

        Assertions.assertEquals(testCategory.name, categories[0].name, "The category name should match")
    }

    @Test
    fun testGetCategories(){
        val amountOfCategories = testCandidate.getCategories()
        Assertions.assertEquals(0, amountOfCategories.size, "There should be 0 categories")
    }

    @Test
    fun testDeleteCategory(){
        val testCategory = Category("privat", 0)
        testCandidate.addCategory(testCategory)
        val deletedCategories = testCandidate.deleteCategory(testCategory.id)

        Assertions.assertEquals(1, deletedCategories, "1 Category should have been deleted")
    }
}