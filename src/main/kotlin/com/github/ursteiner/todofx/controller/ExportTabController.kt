package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.service.CsvExportServiceImpl
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
import com.github.ursteiner.todofx.service.LanguageServiceImpl
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TitledPane
import javafx.stage.FileChooser
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*


class ExportTabController: Initializable {

    private val logger = LoggerFactory.getLogger(ExportTabController::class.java)

    @FXML
    private lateinit var exportPane: TitledPane

    private lateinit var languageService : LanguageServiceImpl

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        languageService = LanguageServiceImpl.getInstance(System.getProperty("user.language"))
        exportPane.text = languageService.getTranslationForKey(TranslationKeys.EXPORT_CAN_BE_STORED_ANYWHERE)
    }

    @FXML
    fun onExportTaskButtonClick(){
        val fileChooser = FileChooser()
        fileChooser.title = "Save"
        fileChooser.initialFileName = "tasks.csv"

        when(val file = fileChooser.showSaveDialog(exportPane.scene.window)){
            null -> logger.info("CSV Exporter: no file selected")
            else -> CsvExportServiceImpl(file).exportTasks(DatabaseServiceImpl.getInstance().getTasks())
        }
    }
}