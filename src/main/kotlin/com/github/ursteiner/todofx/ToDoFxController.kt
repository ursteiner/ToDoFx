package com.github.ursteiner.todofx

import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ResourceBundle


class ToDoFxController : Initializable {

    @FXML
    private lateinit var taskNameInput: TextField

    @FXML
    private lateinit var tableView: TableView<Task>

    private var DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    var datebaseService : DatabaseService = DatabaseService()

    @FXML
    private fun onAddTaskButtonClick() {

        val data: ObservableList<Task> = tableView.getItems()
        if(taskNameInput.text.isNotEmpty()) {
            val newTask : Task = Task(taskNameInput.text, LocalDateTime.now().format(DATE_TIME_FORMAT))
            data.add(newTask)
            datebaseService.addTask(newTask)
        }
        taskNameInput.text = ""

        TableView.CONSTRAINED_RESIZE_POLICY

    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        for(task in datebaseService.getTasks()){
            tableView.items.add(task)
        }
    }
}