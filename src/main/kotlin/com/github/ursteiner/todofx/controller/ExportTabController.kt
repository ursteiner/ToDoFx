package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.AppSettings
import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.service.CsvExportServiceImpl
import com.github.ursteiner.todofx.viewModel.ViewModelProvider
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TitledPane
import javafx.stage.FileChooser
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.util.*


class ExportTabController : CommonController() {

    private val logger = LoggerFactory.getLogger(ExportTabController::class.java)

    @FXML
    private lateinit var exportPane: TitledPane

    @FXML
    private lateinit var exportButton: Button

    private val taskViewModel = ViewModelProvider.taskViewModel
    private val settingsModel = ViewModelProvider.settingsViewModel

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        initTranslations()
    }

    @FXML
    fun onExportTaskButtonClick() {
        val fileChooser = FileChooser().apply {
            initialDirectory
            title = getTranslation(TranslationKeys.SAVE_EXPORTED_FILE)
            initialFileName = "tasks.csv"
            extensionFilters.add(
                FileChooser.ExtensionFilter("CSV", "*.csv"),
            )
            val lastExportPath = settingsModel.getSetting(AppSettings.LAST_EXPORT_PATH)
            if(lastExportPath != null){
                initialDirectory = File(lastExportPath)
            }
        }

        when (val file = fileChooser.showSaveDialog(exportPane.scene.window)) {
            null -> logger.info("CSV Exporter: no file selected")
            else -> {
                CsvExportServiceImpl(file).exportTasks(taskViewModel.getTaskList())
                settingsModel.updateSetting(AppSettings.LAST_EXPORT_PATH, file.parent)
            }
        }
    }

    override fun initTranslations() {
        exportPane.text = getTranslation(TranslationKeys.EXPORT_CAN_BE_STORED_ANYWHERE)
        exportButton.text = "💾 ${getTranslation(TranslationKeys.EXPORT)}"
    }
}