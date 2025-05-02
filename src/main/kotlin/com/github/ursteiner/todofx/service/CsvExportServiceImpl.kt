package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.Task
import java.io.BufferedWriter
import java.io.FileWriter

class CsvExportServiceImpl : ExportService {

    private val writer: BufferedWriter

    constructor(bufferedWriter: BufferedWriter){
        writer = bufferedWriter
    }

    constructor(filePath: String){
        writer = BufferedWriter(FileWriter(filePath))
    }

    override fun exportTasks(tasks: MutableList<Task>) {
        writer.write(""""Id","Description","Date","Resolved"""")
        writer.newLine()
        tasks.forEach {
            writer.write("${it.getIdProperty()},\"${it.getNameProperty()}\",${it.getDateProperty()},${it.getIsDoneProperty()}")
            writer.newLine()
        }
        writer.flush()
        writer.close()
    }
}