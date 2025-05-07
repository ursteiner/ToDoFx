package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.Task
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import java.io.BufferedWriter


class CsvExportServiceImplTest {

    private lateinit var testCandidate: CsvExportServiceImpl
    private lateinit var mockWriter: BufferedWriter

    @BeforeEach
    fun setUp() {
        mockWriter = Mockito.mock(BufferedWriter::class.java)
        testCandidate = CsvExportServiceImpl(mockWriter)
    }

    @Test
    fun testExportTasks(){
        val task1 = Task("Task1", "2025-05-01 20:20", 11, false)
        val tasks = mutableListOf<Task>()
        tasks.add(task1)

        testCandidate.exportTasks(tasks)

        val textLineCaptor = ArgumentCaptor.forClass(String::class.java)

        Mockito.verify(mockWriter, Mockito.atLeast(2)).write(textLineCaptor.capture())
        Mockito.verify(mockWriter, Mockito.atLeast(2)).newLine()
        Mockito.verify(mockWriter).flush()
        Mockito.verify(mockWriter).close()

        val keys: MutableList<String> = textLineCaptor.getAllValues()
        Assertions.assertEquals(keys[0], """"Id","Description","Date","Resolved"""")
        Assertions.assertEquals(keys[1], """11,"Task1",2025-05-01 20:20,false""")
    }
}