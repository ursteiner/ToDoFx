package com.github.ursteiner.todofx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class ToDoFxApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(ToDoFxApplication::class.java.getResource("toDoFx-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 700.0, 400.0)
        stage.title = "ToDoFX"
        stage.icons.add(Image(ToDoFxApplication::class.java.getResourceAsStream("appIcon.png")))
        stage.scene = scene
        stage.resizableProperty().set(false)
        stage.show()
    }
}

fun main() {
    Application.launch(ToDoFxApplication::class.java)
}