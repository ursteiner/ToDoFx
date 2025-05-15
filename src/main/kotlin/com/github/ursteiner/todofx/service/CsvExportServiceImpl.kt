package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.Task
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class CsvExportServiceImpl : ExportService {

    private val writer: BufferedWriter

    constructor(bufferedWriter: BufferedWriter){
        writer = bufferedWriter
    }

    constructor(filePath: File){
        writer = BufferedWriter(FileWriter(filePath))
    }

    override fun exportTasks(tasks: MutableList<Task>) {
        writer.write(""""Id","Description","Category",Date","Resolved","ResolvedDate"""")
        writer.newLine()
        tasks.forEach {
            writer.write("${it.getIdProperty()},\"${it.getNameProperty()}\",\"${it.getCategoryProperty()}\",${it.getDateProperty()},${it.getIsDoneProperty()},${it.getResolvedDate()}")
            writer.newLine()
        }
        writer.flush()
        writer.close()
    }
}