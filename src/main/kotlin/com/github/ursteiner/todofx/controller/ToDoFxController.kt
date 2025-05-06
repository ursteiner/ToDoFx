package com.github.ursteiner.todofx.controller

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

    private var selectedTab: Int = 0

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
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
}