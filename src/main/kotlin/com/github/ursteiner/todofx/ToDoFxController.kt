package com.github.ursteiner.todofx

import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.TableView
import javafx.scene.control.TextField


class ToDoFxController {

    @FXML
    private lateinit var taskNameInput: TextField

    @FXML
    private lateinit var tableView: TableView<Task>

    @FXML
    private fun onAddTaskButtonClick() {
        val data: ObservableList<Task> = tableView.getItems()
        if(taskNameInput.text.isNotEmpty()) {
            data.add(Task(taskNameInput.text))
        }
        taskNameInput.text = ""
    }
}