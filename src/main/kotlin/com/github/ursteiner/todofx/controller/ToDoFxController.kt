package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.service.DatabaseService
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
import com.github.ursteiner.todofx.view.FxMessageDialog
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
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

    private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val databaseService : DatabaseService = DatabaseServiceImpl("~/tasks")
    private val isDoneTextIcon = "✔"
    private val tasks = mutableListOf<Task>()

    override fun initialize(p0: URL?, p1: ResourceBundle?){
        for(task in databaseService.getTasks()){
            tasks.add(task)
            if(task.getIsDoneProperty()){
                task.setIsDoneIconProperty(isDoneTextIcon)
            }
        }

        onHideDoneTaskCheckBoxChanged()
    }

    @FXML
    private fun onAddTaskButtonClick(){
        if(taskNameInput.text.isEmpty()){
            showDialogMessage("Task", "Please first fill in a name of the task!")
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

        databaseService.deleteTask(selectedTask.getIdProperty())
        tasks.remove(selectedTask)
        onHideDoneTaskCheckBoxChanged()
        taskUpdateArea.text = ""
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
        databaseService.updateTask(selectedTask, selectedTask.getNameProperty())

        onHideDoneTaskCheckBoxChanged()
    }
    @FXML
    private fun onUpdateTaskButtonClick(){
        val selectedTask = tableView.selectionModel.selectedItem
        if(selectedTask == null) {
            showDialogMessageFirstSelectATask()
            return
        }
        System.out.println(selectedTask.getIdProperty());
        System.out.println(taskUpdateArea.text);
        databaseService.updateTask(selectedTask, taskUpdateArea.text);
        refreshTableView();
    }
    @FXML
    private fun refreshTableView() {
        val updatedTaskList = databaseService.getTasks();
        tableView.items.setAll(updatedTaskList);
    }

    private fun setTaskDoneIcon(task: Task){
        if(task.getIsDoneProperty()){
            task.setIsDoneIconProperty(isDoneTextIcon)
        }else{
            task.setIsDoneIconProperty("")
        }
    }

    @FXML
    private fun onTaskSelected(){
        val selectedTask = tableView.selectionModel.selectedItem
        if(selectedTask != null) {
            taskUpdateArea.text = selectedTask.getNameProperty()
            taskUpdateArea.isVisible = true;
            taskUpdateArea.isManaged = true
            updateTaskButton.isVisible = true;
            updateTaskButton.isManaged = true
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
        showDialogMessage("Task", "Please first select a task in the table!")
    }

    fun showDialogMessage(title: String, content: String){
        FxMessageDialog.createMessageDialog(title, content)
    }
}