package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.service.CsvExportServiceImpl
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
import javafx.fxml.FXML

class ExportTabController {

    @FXML
    fun onExportTaskButtonClick(){
        CsvExportServiceImpl("tasks.csv").exportTasks(DatabaseServiceImpl.getInstance().getTasks())
    }
}