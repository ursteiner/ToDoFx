package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.service.CsvExportServiceImpl
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
import com.github.ursteiner.todofx.service.LanguageServiceImpl
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.ResourceBundle

class ExportTabController: Initializable {

    @FXML
    private lateinit var exportLabel: Label

    private lateinit var languageService : LanguageServiceImpl

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        languageService = LanguageServiceImpl.getInstance(System.getProperty("user.language"))
        exportLabel.text = languageService.getTranslationForKey(TranslationKeys.EXPORT_IS_STORED_TO_APPLICATION_FOLDER)
    }

    @FXML
    fun onExportTaskButtonClick(){
        CsvExportServiceImpl("tasks.csv").exportTasks(DatabaseServiceImpl.getInstance().getTasks())
    }
}