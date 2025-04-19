package com.github.ursteiner.todofx

import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ToDoFxController : Initializable {

    @FXML
    private lateinit var taskNameInput: TextArea

    @FXML
    private lateinit var taskPreview: Label

    @FXML
    private lateinit var tableView: TableView<Task>

    private var DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    var databaseService : DatabaseService = DatabaseService()

    @FXML
    private fun onAddTaskButtonClick() {

        val data: ObservableList<Task> = tableView.getItems()
        if(taskNameInput.text.isNotEmpty()) {
            val newTask : Task = Task(taskNameInput.text, LocalDateTime.now().format(DATE_TIME_FORMAT), -1)
            data.add(newTask)
            databaseService.addTask(newTask)
        }else {
            showDialogMessage("Task", "Please first fill in a name of the task!")
        }
        taskNameInput.text = ""

        TableView.CONSTRAINED_RESIZE_POLICY

    }

    @FXML
    private fun onDeleteTaskButtonClick() {
        val selectedTask = tableView.selectionModel.selectedItem
        if(selectedTask != null) {
            databaseService.deleteTask(selectedTask.getIdProperty())
            tableView.items.remove(selectedTask)
            taskPreview.text = ""
        }else {
            showDialogMessage("Task", "Please first select a task in the table!")
        }

    }

    @FXML
    private fun onTaskSelected() {
        val selectedTask = tableView.selectionModel.selectedItem
        if(selectedTask != null) {
            taskPreview.text = selectedTask.getNameProperty()
        }
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        for(task in databaseService.getTasks()){
            tableView.items.add(task)
        }
    }

    fun showDialogMessage(title: String, content: String){
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = title
        alert.contentText = content
        alert.showAndWait()
    }
}