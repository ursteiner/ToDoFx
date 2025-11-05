package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.NaiveBayesModelData
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class NaiveBayesClassificationTest {

    private lateinit var testCandidate: NaiveBayesClassification

    @BeforeEach
    fun setUp() {
        testCandidate = NaiveBayesClassification()
        testCandidate.train("Buy milk", "Shopping")
        testCandidate.train("Order groceries", "Shopping")
        testCandidate.train("Write project report", "Work")
        testCandidate.train("Prepare slides", "Work")
        testCandidate.train("Call plumber", "Home")
        testCandidate.train("Clean kitchen", "Home")
        testCandidate.train("Cook in the kitchen", "Home")
    }

    @Test
    fun shouldSelectShoppingAsCategory(){
        assertEquals("Shopping", testCandidate.predict("Order milk"))
    }

    @Test
    fun shouldSelectHomeAsCategory(){
        assertEquals("Home", testCandidate.predict("Buy a new kitchen"))
    }

    @Test
    fun shouldReturnNull_whenTextIsEmpty(){
        assertNull(testCandidate.predict(""))
    }

    @Test
    fun shouldContainCorrectModel_whenExported() {
        val modelData = Json.decodeFromString<NaiveBayesModelData>(testCandidate.exportModel())
        assertEquals(7,modelData.totalTasks)
        assertEquals(3,modelData.categoryCounts.keys.size)
        assertEquals(16,modelData.vocabulary.size)
        assertEquals(2, modelData.wordCounts["Home"]?.get("kitchen"))
        assertEquals(1, modelData.wordCounts["Shopping"]?.get("order"))
    }



}