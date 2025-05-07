package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.service.CsvExportServiceImpl
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
import javafx.fxml.FXML
import javafx.scene.control.TitledPane
import javafx.stage.FileChooser
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*


class ExportTabController: CommonController() {

    private val logger = LoggerFactory.getLogger(ExportTabController::class.java)

    @FXML
    private lateinit var exportPane: TitledPane

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        exportPane.text = getTranslation(TranslationKeys.EXPORT_CAN_BE_STORED_ANYWHERE)
    }

    @FXML
    fun onExportTaskButtonClick(){
        val fileChooser = FileChooser()
        with(fileChooser){
            title = "Save"
            initialFileName = "tasks.csv"
            extensionFilters.add(
                FileChooser.ExtensionFilter("CSV", "*.csv"),
            )
        }

        when(val file = fileChooser.showSaveDialog(exportPane.scene.window)){
            null -> logger.info("CSV Exporter: no file selected")
            else -> CsvExportServiceImpl(file).exportTasks(DatabaseServiceImpl.getInstance().getTasks())
        }
    }
}