package com.github.ursteiner.todofx.viewModel

import com.github.ursteiner.todofx.database.CategoryRepository
import com.github.ursteiner.todofx.model.Category
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class CategoryViewModelTest {
    private lateinit var categoryDb: CategoryRepository
    private lateinit var viewModel: CategoryViewModel

    @BeforeEach
    fun setUp() {
        categoryDb = mock(CategoryRepository::class.java)
        viewModel = CategoryViewModel(categoryDb)
    }

    @Test
    fun shouldContainCategories_when_categoriesLoaded() {
        val mockCategories = listOf(Category("Work", 0), Category("Home",1))
        `when`(categoryDb.getCategories()).thenReturn(mockCategories)

        viewModel.loadCategories()

        assertEquals(2, viewModel.categories.size)
        assertEquals("Work", viewModel.categories.first().name)
        assertEquals(listOf("Work", "Home"), viewModel.categoryNames)
    }

    @Test
    fun shouldContainCategory_when_CategoryAdded() {
        val category = Category("Fitness",3)
        `when`(categoryDb.getCategories()).thenReturn(listOf(category))

        viewModel.addCategory(category)

        verify(categoryDb).addCategory(category)
        assertEquals(1, viewModel.categories.size)
        assertEquals("Fitness", viewModel.categories.first().name)
    }

    @Test
    fun shouldReloadCategories_when_categoryUpdated() {
        val category = Category("Updated",1)
        `when`(categoryDb.getCategories()).thenReturn(listOf(category))

        viewModel.updateCategory(category)

        verify(categoryDb).updateCategory(category)
        assertEquals("Updated", viewModel.categories.first().name)
    }

    @Test
    fun shouldRemoveCategory_when_CategoryDeleted() {
        val category = Category("ToDelete",1)
        `when`(categoryDb.getCategories()).thenReturn(emptyList())

        viewModel.deleteCategory(category)

        verify(categoryDb).deleteCategory(category.id)
        assertTrue(viewModel.categories.isEmpty())
    }
}