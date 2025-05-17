package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.Tabs
import com.github.ursteiner.todofx.constants.TranslationKeys
import javafx.fxml.FXML
import javafx.scene.control.TabPane
import javafx.scene.layout.AnchorPane
import java.net.URL
import java.util.ResourceBundle

class ToDoFxController: CommonController() {

    lateinit var statisticsTabPage: AnchorPane

    lateinit var settingsTabPage: AnchorPane

    @FXML
    private lateinit var tabPane : TabPane

    @FXML
    private lateinit var statisticsTabPageController: StatisticTabController

    @FXML
    private lateinit var settingsTabPageController: SettingsTabController

    private var selectedTab = Tabs.TASKS

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        with(tabPane){
            tabs[Tabs.TASKS.index].text = getTranslation(TranslationKeys.TASKS)
            tabs[Tabs.STATISTICS.index].text = getTranslation(TranslationKeys.STATISTIC)
            tabs[Tabs.EXPORT.index].text = getTranslation(TranslationKeys.EXPORT)
            tabs[Tabs.SETTINGS.index].text = getTranslation(TranslationKeys.SETTINGS)
        }
    }

    @FXML
    fun onTabClicked(){
        val currentTab = Tabs.entries[tabPane.selectionModel.selectedIndex]

        if(currentTab == selectedTab){
            return
        }

        when(tabPane.selectionModel.selectedIndex){
            Tabs.STATISTICS.index -> {
                statisticsTabPageController.buildPieChart()
                statisticsTabPageController.buildBarChart()
            }
            Tabs.SETTINGS.index -> {
                settingsTabPageController.initCategories()
            }
        }

        selectedTab = currentTab
    }
}