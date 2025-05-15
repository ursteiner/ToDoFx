package com.github.ursteiner.todofx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class ToDoFxApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(ToDoFxApplication::class.java.getResource("toDoFx-view.fxml"))
        val startScene = Scene(fxmlLoader.load(), 840.0, 520.0)
        with(stage){
            title = "ToDoFX"
            icons.add(Image(ToDoFxApplication::class.java.getResourceAsStream("appIcon.png")))
            scene = startScene
            resizableProperty().set(false)
            show()
        }
    }
}

fun main() {
    Application.launch(ToDoFxApplication::class.java)
}