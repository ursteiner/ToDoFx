package com.github.ursteiner.todofx.integration

import com.github.ursteiner.todofx.ToDoFxApplication
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.testfx.api.FxAssert
import org.testfx.framework.junit5.ApplicationTest
import org.testfx.matcher.base.NodeMatchers

class ToDoFxApplicationTest : ApplicationTest() {

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(ToDoFxApplication::class.java.getResource("toDoFx-view.fxml"))
        val startScene = Scene(fxmlLoader.load(), 940.0, 520.0)
        with(stage){
            title = "ToDoFX"
            icons.add(Image(ToDoFxApplication::class.java.getResourceAsStream("appIcon.png")))
            scene = startScene
            resizableProperty().set(false)
            show()
        }
    }

    @Test
    fun buttons_shouldBeVisible_whenStarted(){
        FxAssert.verifyThat("#addTaskButton", NodeMatchers.isVisible())
        FxAssert.verifyThat("#searchButton", NodeMatchers.isVisible())
        FxAssert.verifyThat("#resolveTaskButton", NodeMatchers.isVisible())
        FxAssert.verifyThat("#deleteTaskButton", NodeMatchers.isVisible())
    }

    @Test
    fun buttons_shouldBeInvisible_whenStarted(){
        FxAssert.verifyThat("#updateTaskButton", NodeMatchers.isInvisible())
    }

}