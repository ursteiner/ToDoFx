package com.github.ursteiner.todofx.service

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.atLeast
import org.junit.jupiter.api.Assertions.assertEquals

import com.github.ursteiner.todofx.model.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import java.io.BufferedWriter


class CsvExportServiceImplTest {

    private lateinit var testCandidate: CsvExportServiceImpl
    private lateinit var mockWriter: BufferedWriter

    @BeforeEach
    fun setUp() {
        mockWriter = mock(BufferedWriter::class.java)
        testCandidate = CsvExportServiceImpl(mockWriter)
    }

    @Test
    fun testExportTasks() {
        val task1 = Task("Task1", "2025-05-01 20:20", "work", 11, false)
        val tasks = mutableListOf<Task>()
        tasks.add(task1)

        testCandidate.exportTasks(tasks)

        val textLineCaptor = ArgumentCaptor.forClass(String::class.java)

        verify(mockWriter, atLeast(2)).write(textLineCaptor.capture())
        verify(mockWriter, atLeast(2)).newLine()
        verify(mockWriter).flush()
        verify(mockWriter).close()

        val keys: MutableList<String> = textLineCaptor.getAllValues()
        assertEquals(keys[0], """"Id","Description","Category","Date","Resolved","ResolvedDate"""")
        assertEquals(keys[1], """11,"Task1","work",2025-05-01 20:20,false,""")
    }
}