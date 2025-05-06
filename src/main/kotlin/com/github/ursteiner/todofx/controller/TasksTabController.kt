package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.view.FxMessageDialog
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.CheckBox
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.TitledPane
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ResourceBundle

class TasksTabController : CommonController() {
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
    private lateinit var searchButton: Button

    @FXML
    private lateinit var searchTextField: TextField

    @FXML
    private lateinit var idColumn: TableColumn<String, String>

    @FXML
    private lateinit var descriptionColumn: TableColumn<String, String>

    @FXML
    private lateinit var dateColumn: TableColumn<String, String>

    @FXML
    private lateinit var doneColumn: TableColumn<String, String>

    @FXML
    private lateinit var editTaskPane: TitledPane

    @FXML
    private lateinit var newTaskPane: TitledPane

    private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val isDoneTextIcon = "✔"
    private val tasks = mutableListOf<Task>()

    override fun initialize(p0: URL?, p1: ResourceBundle?){
        initializeFieldNames()
        getTasksBasedOnFilters()
    }

    fun initializeFieldNames(){
        hideDoneTasksCheckBox.text = getTranslation(TranslationKeys.HIDE_DONE_TASKS)

        editTaskPane.text = getTranslation(TranslationKeys.EDIT_TASK_DESCRIPTION)
        newTaskPane.text = getTranslation(TranslationKeys.NEW_TASK_DESCRIPTION)

        addTaskButton.text = getTranslation(TranslationKeys.ADD_TASK)
        resolveTaskButton.text = getTranslation(TranslationKeys.RESOLVE_TASK)
        deleteTaskButton.text = getTranslation(TranslationKeys.DELETE_TASK)
        updateTaskButton.text = getTranslation(TranslationKeys.UPDATE_TASK)
        searchButton.text = getTranslation(TranslationKeys.SEARCH)

        idColumn.text = getTranslation(TranslationKeys.ID)
        descriptionColumn.text = getTranslation(TranslationKeys.DESCRIPTION)
        dateColumn.text = getTranslation(TranslationKeys.DATE)
        doneColumn.text = getTranslation(TranslationKeys.DONE)
    }

    @Suppress("unused")
    @FXML
    private fun onAddTaskButtonClick(){
        if(taskNameInput.text.isEmpty()){
            showDialogMessage(getTranslation(TranslationKeys.TASK),
                getTranslation(TranslationKeys.FIRST_FILL_IN_A_DESCRIPTION))
            return
        }

        val newTask = Task(taskNameInput.text, LocalDateTime.now().format(dateTimeFormat), -1)
        tasks.add(newTask)
        getDatabase().addTask(newTask)
        getTasksBasedOnFilters()
        taskNameInput.text = ""
    }

    @Suppress("unused")
    @FXML
    private fun onSearchButtonClick(){
        if(searchTextField.text.isEmpty()){
            FxMessageDialog.createMessageDialog(getTranslation(TranslationKeys.MISSING_SEARCH_TERM),
                getTranslation(TranslationKeys.PLEASE_FILL_IN_SEARCH_TERM))
            return
        }

        tasks.clear()
        tasks.addAll(getDatabase().getSearchedTasks(("%${searchTextField.text}%".lowercase())))

        tasks.filter { it.getIsDoneProperty() }
            .map { it.setIsDoneIconProperty(isDoneTextIcon) }

        tableView.items = FXCollections.observableList(tasks)
        tableView.refresh()
    }

    @Suppress("unused")
    @FXML
    private fun onClearSearchButton(){
        if(searchTextField.text.isNotEmpty()) {
            searchTextField.text = ""
            getTasksBasedOnFilters()
        }
    }

    @Suppress("unused")
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
            getDatabase().deleteTask(selectedTask.getIdProperty())

            getTasksBasedOnFilters()
            setVisibilityUpdateTask(false)
        }
    }

    @Suppress("unused")
    @FXML
    private fun onCompletedTaskButtonClick(){
        val selectedTask = tableView.selectionModel.selectedItem
        if(selectedTask == null) {
            showDialogMessageFirstSelectATask()
            return
        }

        selectedTask.setIsDoneProperty(!selectedTask.getIsDoneProperty())
        getDatabase().updateTask(selectedTask)

        if(hideDoneTasksCheckBox.isSelected){
            setVisibilityUpdateTask(false)
        }

        getTasksBasedOnFilters()
    }

    @Suppress("unused")
    @FXML
    private fun onUpdateTaskButtonClick(){
        val selectedTask = tableView.selectionModel.selectedItem
        if(selectedTask == null) {
            showDialogMessageFirstSelectATask()
            return
        }
        if(selectedTask.getNameProperty() != taskUpdateArea.text){
            selectedTask.setNameProperty(taskUpdateArea.text)
            getDatabase().updateTask(selectedTask)
        }

        setVisibilityUpdateTask(false)
        tableView.refresh()
    }

    private fun setVisibilityUpdateTask(isVisible: Boolean){
        taskUpdateArea.isVisible = isVisible
        updateTaskButton.isVisible = isVisible
        editTaskPane.isVisible = isVisible
    }

    @Suppress("unused")
    @FXML
    private fun onTaskSelected(){
        val selectedTask = tableView.selectionModel.selectedItem
        if(selectedTask != null) {
            taskUpdateArea.text = selectedTask.getNameProperty()
            setVisibilityUpdateTask(true)
        }
    }

    @FXML
    private fun getTasksBasedOnFilters(){
        tasks.clear()

        if(hideDoneTasksCheckBox.isSelected){
            tasks.addAll(getDatabase().getOpenTasks())
            setVisibilityUpdateTask(false)
        }else{
            tasks.addAll(getDatabase().getTasks())
            tasks.filter { it.getIsDoneProperty() }
                .map { it.setIsDoneIconProperty(isDoneTextIcon) }
        }

        tableView.items = FXCollections.observableList(tasks)
        tableView.refresh()
    }

    fun showDialogMessageFirstSelectATask(){
        showDialogMessage(getTranslation(TranslationKeys.TASK),
            getTranslation(TranslationKeys.FIRST_SELECT_TASK_IN_TABLE))
    }

    fun showDialogMessage(title: String, content: String){
        FxMessageDialog.createMessageDialog(title, content)
    }
}