package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
import com.github.ursteiner.todofx.service.LanguageServiceImpl
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.chart.PieChart
import javafx.scene.control.Tooltip
import java.net.URL
import java.util.ResourceBundle

class StatisticTabController: Initializable {

    @FXML
    private lateinit var pieChart: PieChart

    private lateinit var databaseService : DatabaseServiceImpl
    private lateinit var languageService : LanguageServiceImpl

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        languageService = LanguageServiceImpl.getInstance(System.getProperty("user.language"))
        databaseService = DatabaseServiceImpl.getInstance()

        pieChart.title = getTranslation(TranslationKeys.STATISTIC)
    }

    fun buildPieChart(){
        val pieChartData = FXCollections.observableArrayList(
            PieChart.Data(getTranslation(TranslationKeys.OPEN_TASKS),
                databaseService.getAmountOfOpenTasks().toDouble()),
            PieChart.Data(getTranslation(TranslationKeys.RESOLVED_TASKS),
                databaseService.getAmountOfResolvedTasks().toDouble())
        )
        pieChart.data = pieChartData
        pieChart.data.forEach {
            addToolTipToNode(it.node, it.name + ": " + String.format("%.0f", it.pieValue))
        }
    }

    private fun addToolTipToNode(node: Node, tooltipText: String){
        val tooltip = Tooltip(tooltipText)
        Tooltip.install(node, tooltip)
    }

    fun getTranslation(key: TranslationKeys): String{
        return languageService.getTranslationForKey(key)
    }
}