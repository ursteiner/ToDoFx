package com.github.ursteiner.todofx

import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ToDoFxController {

    @FXML
    private lateinit var taskNameInput: TextField

    @FXML
    private lateinit var tableView: TableView<Task>

    private var DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    @FXML
    private fun onAddTaskButtonClick() {
        val data: ObservableList<Task> = tableView.getItems()
        if(taskNameInput.text.isNotEmpty()) {
            data.add(Task(taskNameInput.text, LocalDateTime.now().format(DATE_TIME_FORMAT)))
        }
        taskNameInput.text = ""

        TableView.CONSTRAINED_RESIZE_POLICY

    }
}