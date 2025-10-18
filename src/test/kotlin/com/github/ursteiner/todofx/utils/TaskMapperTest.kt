package com.github.ursteiner.todofx.utils

import org.junit.jupiter.api.Assertions.assertEquals
import com.github.ursteiner.todofx.model.FXTask
import com.github.ursteiner.todofx.model.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class TaskMapperTest {

    private lateinit var testCandidate: TaskMapper

    @BeforeEach
    fun setUp() {
        testCandidate = TaskMapper
    }

    @Test
    fun testMapFxTaskToTask() {
        val taskFx = FXTask(
            "Write some Tests",
            "2025-10-18 10:10",
            123,
            false,
            "",
            "Programming"
        )

        val task = testCandidate.mapFxTaskToTask(taskFx)

        assertEquals("Write some Tests", task.name)
        assertEquals("2025-10-18 10:10", task.date)
        assertEquals(123, task.id)
        assertEquals(false, task.isDone)
        assertEquals("", task.resolvedDate)
        assertEquals("Programming", task.category)
    }

    @Test
    fun testMapTaskToFxTask() {
        val task = Task(
            "Go to the dentist",
            "2025-01-01 08:00",
            1212,
            true,
            "2025-10-01 17:00",
            "No category"
        )

        val fxTask = testCandidate.mapTaskToFxTask(task)

        assertEquals("Go to the dentist", fxTask.getNameProperty())
        assertEquals("2025-01-01 08:00", fxTask.getDateProperty())
        assertEquals(1212, fxTask.getIdProperty())
        assertEquals(true, fxTask.getIsDoneProperty())
        assertEquals("2025-10-01 17:00", fxTask.getResolvedDateProperty())
        assertEquals("No category", fxTask.getCategoryProperty())
    }
}