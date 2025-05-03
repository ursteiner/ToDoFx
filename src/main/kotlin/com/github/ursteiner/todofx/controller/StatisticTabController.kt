package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
import com.github.ursteiner.todofx.service.LanguageServiceImpl
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.chart.*
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.Tooltip
import java.net.URL
import java.util.*


class StatisticTabController: Initializable {

    @FXML
    private lateinit var pieChart: PieChart

    @FXML
    private lateinit var barChart: BarChart<String,Int>

    @FXML
    private lateinit var yAxis: NumberAxis

    @FXML
    private lateinit var xAxis: CategoryAxis

    private lateinit var databaseService : DatabaseServiceImpl
    private lateinit var languageService : LanguageServiceImpl

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        languageService = LanguageServiceImpl.getInstance(System.getProperty("user.language"))
        databaseService = DatabaseServiceImpl.getInstance()

        pieChart.title = getTranslation(TranslationKeys.STATISTIC)

        barChart.title = getTranslation(TranslationKeys.TASKS_PER_MONTH)
        xAxis.label = getTranslation(TranslationKeys.MONTH)
        //workaround for bad label positioning
        xAxis.animated = false
        yAxis.label = "#Tasks"
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

    fun buildBarChart(){
        barChart.data.clear()

        val series = Series<String?, Int?>()
        series.name = getTranslation(TranslationKeys.TASKS_PER_MONTH)

        databaseService.getTasksPerMonth(12).forEach {
            entry ->
            series.data.add(XYChart.Data<String?, Int?>(entry.key, entry.value))
        }

        barChart.data.addAll(series)
    }

    private fun addToolTipToNode(node: Node, tooltipText: String){
        val tooltip = Tooltip(tooltipText)
        Tooltip.install(node, tooltip)
    }

    fun getTranslation(key: TranslationKeys): String{
        return languageService.getTranslationForKey(key)
    }
}