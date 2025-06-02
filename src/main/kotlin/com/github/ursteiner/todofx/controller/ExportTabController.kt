package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.service.CsvExportServiceImpl
import com.github.ursteiner.todofx.database.TaskDatabaseServiceImpl
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TitledPane
import javafx.stage.FileChooser
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*


class ExportTabController: CommonController() {

    private val logger = LoggerFactory.getLogger(ExportTabController::class.java)

    @FXML
    private lateinit var exportPane: TitledPane

    @FXML
    private lateinit var exportButton: Button

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        initTranslations()
    }

    @FXML
    fun onExportTaskButtonClick(){
        val fileChooser = FileChooser()
        with(fileChooser){
            title = getTranslation(TranslationKeys.SAVE_EXPORTED_FILE)
            initialFileName = "tasks.csv"
            extensionFilters.add(
                FileChooser.ExtensionFilter("CSV", "*.csv"),
            )
        }

        when(val file = fileChooser.showSaveDialog(exportPane.scene.window)){
            null -> logger.info("CSV Exporter: no file selected")
            else -> CsvExportServiceImpl(file).exportTasks(TaskDatabaseServiceImpl.getInstance(defaultDataBasePathName).getTasks())
        }
    }

    override fun initTranslations() {
        exportPane.text = getTranslation(TranslationKeys.EXPORT_CAN_BE_STORED_ANYWHERE)
        exportButton.text = "💾 ${getTranslation(TranslationKeys.EXPORT)}"
    }
}