package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.Tabs
import com.github.ursteiner.todofx.constants.TranslationKeys
import javafx.application.HostServices
import javafx.fxml.FXML
import javafx.scene.control.TabPane
import javafx.scene.layout.AnchorPane
import java.net.URL
import java.util.ResourceBundle

class ToDoFxController : CommonController() {

    //prevents unused warning, although the controllers are used
    lateinit var settingsTabPage: AnchorPane
    lateinit var exportTabPage: AnchorPane
    lateinit var tasksTabPage: AnchorPane
    lateinit var statisticsTabPage: AnchorPane

    lateinit var hostServices: HostServices

    @FXML
    private lateinit var tabPane: TabPane

    @FXML
    private lateinit var statisticsTabPageController: StatisticTabController

    @FXML
    private lateinit var settingsTabPageController: SettingsTabController

    @FXML
    private lateinit var tasksTabPageController: TasksTabController

    @FXML
    private lateinit var exportTabPageController: ExportTabController

    private var selectedTab = Tabs.TASKS

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        settingsTabPageController.toDoFxController = this
        initTranslations()
    }

    @FXML
    fun onTabClicked() {
        val currentTab = Tabs.entries[tabPane.selectionModel.selectedIndex]

        if (currentTab == selectedTab) {
            return
        }

        when (currentTab) {
            Tabs.TASKS -> {
                tasksTabPageController.initializeDropDownsAndTranslations()
            }

            Tabs.STATISTICS -> {
                statisticsTabPageController.initialize(null, null)
                statisticsTabPageController.buildPieChartResolvedOpen()
                statisticsTabPageController.buildBarChartTasksPerMonth()
                statisticsTabPageController.buildPieChartTasksPerCategory()
            }

            Tabs.EXPORT -> {
                exportTabPageController.initialize(null, null)
            }

            Tabs.SETTINGS -> {
                settingsTabPageController.hostServices = hostServices
                settingsTabPageController.initTranslations()
                settingsTabPageController.initCategories()
            }
        }

        selectedTab = currentTab
    }

    override fun initTranslations() {
        with(tabPane) {
            tabs[Tabs.TASKS.index].text = getTranslation(TranslationKeys.TASKS)
            tabs[Tabs.STATISTICS.index].text = getTranslation(TranslationKeys.STATISTIC)
            tabs[Tabs.EXPORT.index].text = getTranslation(TranslationKeys.EXPORT)
            tabs[Tabs.SETTINGS.index].text = getTranslation(TranslationKeys.SETTINGS)
        }
    }
}