package com.github.ursteiner.todofx

import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.service.DatabaseService
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test

class DatabaseServiceImplTest {

    val testCandidate: DatabaseService = DatabaseServiceImpl("./testTasks")

    @BeforeEach
    fun cleanupTestDatabase() {
        for (task in testCandidate.getTasks()) {
            testCandidate.deleteTask(task.getIdProperty())
            println("Deleted task: ${task.getIdProperty()}")
        }
    }

    @Test
    fun testAddTask() {
        val testTask = Task("This is a Test Task", "2025-04-20 10:00")
        testCandidate.addTask(testTask)

        val databaseTasks = testCandidate.getTasks()

        Assertions.assertEquals(1,
            databaseTasks.size,
            "There should be one task available"
        )
        Assertions.assertEquals(
            testTask.getNameProperty(),
            databaseTasks[0].getNameProperty(),
            "Task description should be the same"
        )
        Assertions.assertEquals(
            testTask.getDateProperty(),
            databaseTasks[0].getDateProperty(),
            "Task date should be the same"
        )
    }

    @Test
    fun testDeleteTask() {
        val testTask1 = Task("This is a Test Task", "2025-04-20 10:00")
        testCandidate.addTask(testTask1)

        val testTask2 = Task("This is second Test Task", "2025-04-21 10:00")
        testCandidate.addTask(testTask2)

        val numberOfDeletedTasks = testCandidate.deleteTask(testTask1.getIdProperty())
        Assertions.assertEquals(1,
            numberOfDeletedTasks,
            "One task should have been deleted"
        )
    }

    @Test
    fun testDeleteNoneExistingTask(){
        val tasksDeleted = testCandidate.deleteTask(-1)
        Assertions.assertEquals(0,
            tasksDeleted,
            "There shouldn't be a task with id -1 to delete")
    }

    @Test
    fun testGetTasksNoneAvailable() {
        Assertions.assertEquals(0,
            testCandidate.getTasks().size,
            "There should be no tasks in the test database"
        )
    }

    @Test
    fun testUpdateTask() {
        val testTask = Task("Task1", "2025-04-20 10:00")
        testCandidate.addTask(testTask)

        val testTask2 = Task("This is second Test Task", "2025-04-21 10:00")
        testCandidate.addTask(testTask2)

        testTask.setIsDoneProperty(true)
        val updatedTasks : Int = testCandidate.updateTask(testTask2)

        Assertions.assertEquals(1,
            updatedTasks,
            "1 task should be updated"
        )
    }

    @Test
    fun testGetAmountOfTasks() {
        val testTask1 = Task("Task1", "2025-04-20 10:00", 0, false)
        testCandidate.addTask(testTask1)

        val testTask2 = Task("This is second Test Task", "2025-04-21 10:00", 0, true)
        testCandidate.addTask(testTask2)

        val amountOfResolvedTasks : Long = testCandidate.getAmountOfResolvedTasks()

        Assertions.assertEquals(1,
            amountOfResolvedTasks,
            "1 task should be resolved"
        )
    }
}