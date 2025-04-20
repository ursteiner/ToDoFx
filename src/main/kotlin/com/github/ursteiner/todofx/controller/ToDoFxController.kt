package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.service.DatabaseServiceImpl
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
import java.util.ResourceBundle

class ToDoFxController : Initializable {

    @FXML
    private lateinit var taskNameInput: TextArea

    @FXML
    private lateinit var taskPreview: Label

    @FXML
    private lateinit var tableView: TableView<Task>

    private var DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    var databaseServiceImpl : DatabaseServiceImpl = DatabaseServiceImpl("~/tasks")

    @FXML
    private fun onAddTaskButtonClick() {

        val data: ObservableList<Task> = tableView.getItems()
        if(taskNameInput.text.isNotEmpty()) {
            val newTask : Task = Task(taskNameInput.text, LocalDateTime.now().format(DATE_TIME_FORMAT), -1)
            data.add(newTask)
            databaseServiceImpl.addTask(newTask)
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
            databaseServiceImpl.deleteTask(selectedTask.getIdProperty())
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
        for(task in databaseServiceImpl.getTasks()){
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