package com.github.ursteiner.todofx.service.integration

import com.github.ursteiner.todofx.database.NaiveBayesModelServiceImpl
import com.github.ursteiner.todofx.model.DbConnection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NaiveBayesModelServiceImplTest {

    private val databaseConnection = DbConnection("jdbc:h2:mem:testTasks;DB_CLOSE_DELAY=-1;", "org.h2.Driver", "root", "")
    private val testCandidate = NaiveBayesModelServiceImpl.getInstance(databaseConnection)

    @Test
    fun shouldReturnNullWhenNoModelInDB() {
        assertEquals(
            null,
            testCandidate.getModel()
        )
    }

    @Test
    fun shouldReturnModelWhenAvailable() {
        testCandidate.updateModel("{}")
        assertEquals(
            "{}",
            testCandidate.getModel()
        )
    }

    @Test
    fun shouldSetCreationDateWhenUpdated() {
        val creationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        testCandidate.updateModel("{}")
        assertEquals(true, testCandidate.getModelDate()?.startsWith(creationDate))
    }
}