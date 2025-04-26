package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.AvailableLanguages
import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.service.DatabaseService
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
import com.github.ursteiner.todofx.service.LanguageService
import com.github.ursteiner.todofx.service.LanguageServiceImpl
import com.github.ursteiner.todofx.view.FxMessageDialog
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.chart.PieChart
import javafx.scene.control.*
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ResourceBundle

class ToDoFxController : Initializable {

    @FXML
    private lateinit var taskNameInput: TextArea

    @FXML
    private lateinit var taskUpdateArea: TextArea

    @FXML
    private lateinit var tableView: TableView<Task>

    @FXML
    private lateinit var hideDoneTasksCheckBox: CheckBox

    @FXML
    private lateinit var updateTaskButton: Button

    @FXML
    private lateinit var addTaskButton: Button

    @FXML
    private lateinit var resolveTaskButton: Button

    @FXML
    private lateinit var deleteTaskButton: Button

    @FXML
    private lateinit var idColumn: TableColumn<String, String>

    @FXML
    private lateinit var descriptionColumn: TableColumn<String, String>

    @FXML
    private lateinit var dateColumn: TableColumn<String, String>

    @FXML
    private lateinit var doneColumn: TableColumn<String, String>

    @FXML
    private lateinit var editTaskLabel: Label

    @FXML
    private lateinit var newTaskLabel: Label

    @FXML
    private lateinit var tabs : TabPane

    @FXML
    private lateinit var pieChart: PieChart

    private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val databaseService : DatabaseService = DatabaseServiceImpl("~/tasks")
    private lateinit var languageService : LanguageService
    private val isDoneTextIcon = "✔"
    private val tasks = mutableListOf<Task>()

    override fun initialize(p0: URL?, p1: ResourceBundle?){

        languageService = when(System.getProperty("user.language")){
            "de" -> LanguageServiceImpl(AvailableLanguages.GERMAN)
            else -> LanguageServiceImpl(AvailableLanguages.ENGLISH)
        }

        initializeFieldNames()

        for(task in databaseService.getTasks()){
            tasks.add(task)
            if(task.getIsDoneProperty()){
                task.setIsDoneIconProperty(isDoneTextIcon)
            }
        }

        onHideDoneTaskCheckBoxChanged()
    }

    fun initializeFieldNames(){
        tabs.tabs.get(1).text = getTranslation(TranslationKeys.STATISTIC)

        hideDoneTasksCheckBox.text = getTranslation(TranslationKeys.HIDE_DONE_TASKS)

        editTaskLabel.text = getTranslation(TranslationKeys.EDIT_TASK_DESCRIPTION)
        newTaskLabel.text = getTranslation(TranslationKeys.NEW_TASK_DESCRIPTION)

        addTaskButton.text = getTranslation(TranslationKeys.ADD_TASK)
        resolveTaskButton.text = getTranslation(TranslationKeys.RESOLVE_TASK)
        deleteTaskButton.text = getTranslation(TranslationKeys.DELETE_TASK)
        updateTaskButton.text = getTranslation(TranslationKeys.UPDATE_TASK)

        idColumn.text = getTranslation(TranslationKeys.ID)
        descriptionColumn.text = getTranslation(TranslationKeys.DESCRIPTION)
        dateColumn.text = getTranslation(TranslationKeys.DATE)
        doneColumn.text = getTranslation(TranslationKeys.DONE)

        pieChart.title = getTranslation(TranslationKeys.STATISTIC)
    }

    @FXML
    private fun onAddTaskButtonClick(){
        if(taskNameInput.text.isEmpty()){
            showDialogMessage(getTranslation(TranslationKeys.TASK),
                getTranslation(TranslationKeys.FIRST_FILL_IN_A_DESCRIPTION))
            return
        }

        val newTask = Task(taskNameInput.text, LocalDateTime.now().format(dateTimeFormat), -1)
        tasks.add(newTask)
        databaseService.addTask(newTask)
        onHideDoneTaskCheckBoxChanged()
        taskNameInput.text = ""
    }

    @FXML
    private fun onDeleteTaskButtonClick(){
        val selectedTask = tableView.selectionModel.selectedItem
        if(selectedTask == null){
            showDialogMessageFirstSelectATask()
            return
        }

        val dialogResult = FxMessageDialog.createMessageDialog(
            getTranslation(TranslationKeys.CONFIRM_DELETING_TASK),
            """
                #${getTranslation(TranslationKeys.DO_YOU_WANT_TO_DELETE_THE_TASK)}
                #'${selectedTask.getNameProperty()}'?
                """.trimMargin("#"),
            Alert.AlertType.CONFIRMATION
        )

        if(dialogResult.isPresent && dialogResult.get() == ButtonType.OK) {
            databaseService.deleteTask(selectedTask.getIdProperty())
            tasks.remove(selectedTask)
            onHideDoneTaskCheckBoxChanged()
            taskUpdateArea.text = ""
        }
    }

    @FXML
    private fun onCompletedTaskButtonClick(){
        val selectedTask = tableView.selectionModel.selectedItem
        if(selectedTask == null) {
            showDialogMessageFirstSelectATask()
            return
        }

        selectedTask.setIsDoneProperty(!selectedTask.getIsDoneProperty())
        setTaskDoneIcon(selectedTask)
        databaseService.updateTask(selectedTask)

        onHideDoneTaskCheckBoxChanged()
    }

    @FXML
    private fun onUpdateTaskButtonClick(){
        val selectedTask = tableView.selectionModel.selectedItem
        if(selectedTask == null) {
            showDialogMessageFirstSelectATask()
            return
        }
        if(selectedTask.getNameProperty() != taskUpdateArea.text){
            selectedTask.setNameProperty(taskUpdateArea.text)
            databaseService.updateTask(selectedTask)
        }

        setVisibilityUpdateTask(false)
        onHideDoneTaskCheckBoxChanged()
    }

    @FXML
    private fun onTabClicked(){
        //Tab1 = Statistics
        if(tabs.selectionModel.isSelected(1)){
            buildPieChart()
        }
    }

    private fun buildPieChart(){
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

    private fun setTaskDoneIcon(task: Task){
        if(task.getIsDoneProperty()){
            task.setIsDoneIconProperty(isDoneTextIcon)
        }else{
            task.setIsDoneIconProperty("")
        }
    }

    private fun setVisibilityUpdateTask(isVisible: Boolean){
        taskUpdateArea.isVisible = isVisible
        updateTaskButton.isVisible = isVisible
        editTaskLabel.isVisible = isVisible
    }

    @FXML
    private fun onTaskSelected(){
        val selectedTask = tableView.selectionModel.selectedItem
        if(selectedTask != null) {
            taskUpdateArea.text = selectedTask.getNameProperty()
            setVisibilityUpdateTask(true)
        }
    }

    @FXML
    private fun onHideDoneTaskCheckBoxChanged(){
        if(hideDoneTasksCheckBox.isSelected){
            tableView.items = FXCollections.observableList(tasks.filter { !it.getIsDoneProperty() })
        }else{
            tableView.items = FXCollections.observableList(tasks)
        }
        tableView.refresh()
    }

    fun showDialogMessageFirstSelectATask(){
        showDialogMessage(getTranslation(TranslationKeys.TASK),
            getTranslation(TranslationKeys.FIRST_SELECT_TASK_IN_TABLE))
    }

    fun showDialogMessage(title: String, content: String){
        FxMessageDialog.createMessageDialog(title, content)
    }

    fun getTranslation(key: TranslationKeys): String{
        return languageService.getTranslationForKey(key)
    }
}