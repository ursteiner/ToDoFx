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

    private val numberOfPreviousMonths = 12

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        initTranslations()

        //Axis animation is disabled as workaround due to incorrect axis labeling
        xAxis.animated = false
        yAxis.animated = false
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

        val tasksPerMonth = getTaskDatabase().getTasksPerMonth(numberOfPreviousMonths)

        //fix 0.5 steps in the y-axis
        yAxis.upperBound = ((tasksPerMonth.maxByOrNull { it.value }?.value?.toDouble()) ?: 0.0) + 2.0
        yAxis.minorTickVisibleProperty().set(false)

        tasksPerMonth.forEach {
            series.data.add(XYChart.Data<String?, Int?>(it.key, it.value))
        }

        barChartTasksPerMonth.data.add(series)

        series.data.forEach {
            FxUtils.addToolTipToNode(it.node, "${it.xValue}: ${String.format("%.0f", it.yValue)}")
        }
    }

    override fun initTranslations() {
        openResolvedTitledPane.text = "${getTranslation(TranslationKeys.OPEN_TASKS)}/${getTranslation(TranslationKeys.RESOLVED_TASKS)}"
        tasksPerCategoryTitledPane.text = getTranslation(TranslationKeys.CATEGORIES)
        tasksPerMonthTitledPane.text = getTranslation(TranslationKeys.TASKS_PER_MONTH)

        xAxis.label = getTranslation(TranslationKeys.MONTH)
        yAxis.label = "#${getTranslation(TranslationKeys.TASKS)}"
    }
}