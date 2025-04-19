package com.github.ursteiner.todofx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("toDoFx-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 700.0, 400.0)
        stage.title = "ToDoFX"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}