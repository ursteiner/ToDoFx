package com.github.ursteiner.todofx

import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test

class DatabaseServiceImplTest {

    val testCandidate: DatabaseServiceImpl = DatabaseServiceImpl("./testTasks")

    @BeforeEach
    fun cleanupTestDatabase() {
        for (task in testCandidate.getTasks()) {
            testCandidate.deleteTask(task.getIdProperty())
            println("Deleted task: ${task.getIdProperty()}")
        }
    }

    @Test
    fun testAddTask() {
        val testTask = Task("This is a Test Task", "2025-04-20 10:00", 0)
        testCandidate.addTask(testTask)

        val databaseTasks = testCandidate.getTasks()

        Assertions.assertEquals(1,
            databaseTasks.size,
            "There should be one task available"
        )
        Assertions.assertEquals(
            testTask.getNameProperty(),
            databaseTasks.get(0).getNameProperty(),
            "Task description should be the same"
        )
        Assertions.assertEquals(
            testTask.getDateProperty(),
            databaseTasks.get(0).getDateProperty(),
            "Task date should be the same"
        )
    }

    @Test
    fun testDeleteTask() {
        val testTask = Task("This is a Test Task", "2025-04-20 10:00", 0)
        testCandidate.addTask(testTask)

        val numberOfDeletedTasks = testCandidate.deleteTask(testTask.getIdProperty())
        Assertions.assertEquals(1, numberOfDeletedTasks, "One task should have been deleted")
    }

    @Test
    fun testGetTasks() {
        Assertions.assertEquals(0,
            testCandidate.getTasks().size,
            "There should be no tasks"
        )
    }
}