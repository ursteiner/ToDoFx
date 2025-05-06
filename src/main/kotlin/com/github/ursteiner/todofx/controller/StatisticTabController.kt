package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.chart.*
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.Tooltip
import java.net.URL
import java.util.*


class StatisticTabController: CommonController() {

    @FXML
    private lateinit var pieChart: PieChart

    @FXML
    private lateinit var barChart: BarChart<String,Int>

    @FXML
    private lateinit var yAxis: NumberAxis

    @FXML
    private lateinit var xAxis: CategoryAxis

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
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
                getDatabase().getAmountOfOpenTasks().toDouble()),
            PieChart.Data(getTranslation(TranslationKeys.RESOLVED_TASKS),
                getDatabase().getAmountOfResolvedTasks().toDouble())
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

        getDatabase().getTasksPerMonth(12).forEach {
            entry ->
            series.data.add(XYChart.Data<String?, Int?>(entry.key, entry.value))
        }

        barChart.data.addAll(series)
    }

    private fun addToolTipToNode(node: Node, tooltipText: String){
        val tooltip = Tooltip(tooltipText)
        Tooltip.install(node, tooltip)
    }

}