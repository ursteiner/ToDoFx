package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.service.LanguageServiceImpl
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TabPane
import java.net.URL
import java.util.ResourceBundle

class ToDoFxController : Initializable {

    @FXML
    private lateinit var tabs : TabPane

    @FXML
    private lateinit var statisticsTabPageController: StatisticTabController

    private lateinit var languageService : LanguageServiceImpl

    private var selectedTab: Int = 0

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        languageService = LanguageServiceImpl.getInstance(System.getProperty("user.language"))

        tabs.tabs[1].text = getTranslation(TranslationKeys.STATISTIC)
    }

    @FXML
    fun onTabClicked(){
        if(tabs.selectionModel.isSelected(1) && tabs.selectionModel.selectedIndex != selectedTab){
            statisticsTabPageController.buildPieChart()
            statisticsTabPageController.buildBarChart()
        }
        selectedTab = tabs.selectionModel.selectedIndex
    }

    fun getTranslation(key: TranslationKeys): String{
        return languageService.getTranslationForKey(key)
    }
}