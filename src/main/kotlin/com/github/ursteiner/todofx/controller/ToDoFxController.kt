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

    @FXML
    private lateinit var tabs : TabPane

    @FXML
    private lateinit var statisticsTabPageController: StatisticTabController

    private var selectedTab = Tabs.TASKS

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        tabs.tabs[Tabs.STATISTICS.index].text = getTranslation(TranslationKeys.STATISTIC)
    }

    @FXML
    fun onTabClicked(){
        val currentTab = Tabs.entries[tabs.selectionModel.selectedIndex]

        if(tabs.selectionModel.isSelected(Tabs.STATISTICS.index) && currentTab != selectedTab){
            statisticsTabPageController.buildPieChart()
            statisticsTabPageController.buildBarChart()
        }
        selectedTab = currentTab
    }
}