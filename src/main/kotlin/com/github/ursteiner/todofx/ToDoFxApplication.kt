package com.github.ursteiner.todofx

import com.github.ursteiner.todofx.controller.ToDoFxController
import com.github.ursteiner.todofx.database.DatabaseProvider
import com.github.ursteiner.todofx.model.DbConnection
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class ToDoFxApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(ToDoFxApplication::class.java.getResource("toDoFx-view.fxml"))
        val startScene = Scene(fxmlLoader.load(), 980.0, 520.0)
        with(stage) {
            title = "ToDoFX"
            icons.add(Image(ToDoFxApplication::class.java.getResourceAsStream("appIcon.png")))
            scene = startScene
            minWidth = 980.0
            minHeight = 520.0
            show()
        }

        //needed to open hyperlinks
        //forwarded to toDoFxController and then to SettingsTabController
        val toDoFxController: ToDoFxController = fxmlLoader.getController()
        toDoFxController.hostServices = hostServices
    }
}

fun main() {
    val dbConnection = DbConnection("jdbc:h2:file:~/tasks", "org.h2.Driver", "root", "")
    DatabaseProvider.connect(dbConnection)
    Application.launch(ToDoFxApplication::class.java)
}