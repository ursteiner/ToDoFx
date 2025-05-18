package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.view.FxUtils
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.chart.*
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.TitledPane
import java.net.URL
import java.util.*


class StatisticTabController: CommonController() {

    @FXML
    private lateinit var pieChartOpenResolved: PieChart

    @FXML
    private lateinit var pieChartTasksPerCategory: PieChart

    @FXML
    private lateinit var openResolvedTitledPane: TitledPane

    @FXML
    private lateinit var tasksPerCategoryTitledPane: TitledPane

    @FXML
    private lateinit var tasksPerMonthTitledPane: TitledPane

    @FXML
    private lateinit var barChartTasksPerMonth: BarChart<String,Int>

    @FXML
    private lateinit var yAxis: NumberAxis

    @FXML
    private lateinit var xAxis: CategoryAxis

    private val TASKS_LAST_X_MONTH = 12

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        openResolvedTitledPane.text = "${getTranslation(TranslationKeys.OPEN_TASKS)}/${getTranslation(TranslationKeys.RESOLVED_TASKS)}"
        tasksPerCategoryTitledPane.text = getTranslation(TranslationKeys.CATEGORIES)

        tasksPerMonthTitledPane.text = getTranslation(TranslationKeys.TASKS_PER_MONTH)
        xAxis.label = getTranslation(TranslationKeys.MONTH)
        //workaround for bad label positioning
        xAxis.animated = false
        yAxis.label = "#Tasks"
    }

    fun buildPieChartResolvedOpen(){
        val pieChartData = FXCollections.observableArrayList(
            PieChart.Data(getTranslation(TranslationKeys.OPEN_TASKS),
                getTaskDatabase().getAmountOfOpenTasks().toDouble()),
            PieChart.Data(getTranslation(TranslationKeys.RESOLVED_TASKS),
                getTaskDatabase().getAmountOfResolvedTasks().toDouble())
        )
        pieChartOpenResolved.data = pieChartData
        pieChartOpenResolved.data.forEach {
            FxUtils.addToolTipToNode(it.node, it.name + ": " + String.format("%.0f", it.pieValue))
        }
    }

    fun buildPieChartTasksPerCategory(){
        val categoriesObservable = FXCollections.observableArrayList<PieChart.Data>()
        val categories = getTaskDatabase().getTasksPerCategory()
        categories.keys.forEach {
            key -> categoriesObservable.add(PieChart.Data(key, categories[key]?.toDouble() ?: 0.0))
        }

        pieChartTasksPerCategory.data = categoriesObservable
        pieChartTasksPerCategory.data.forEach {
            FxUtils.addToolTipToNode(it.node, "${it.name}: ${String.format("%.0f", it.pieValue)}")
        }
    }

    fun buildBarChartTasksPerMonth(){
        barChartTasksPerMonth.data.clear()

        val series = Series<String?, Int?>()
        series.name = getTranslation(TranslationKeys.TASKS_PER_MONTH)

        getTaskDatabase().getTasksPerMonth(TASKS_LAST_X_MONTH).forEach {
            series.data.add(XYChart.Data<String?, Int?>(it.key, it.value))
        }

        barChartTasksPerMonth.data.addAll(series)

        series.data.forEach {
            FxUtils.addToolTipToNode(it.node, "${it.xValue}: ${it.yValue}")
        }
    }
}