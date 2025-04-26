package com.github.ursteiner.todofx.view

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import java.util.Optional

object FxMessageDialog {

    fun createMessageDialog(title: String, content: String, type: Alert.AlertType = Alert.AlertType.INFORMATION): Optional<ButtonType>{
        val alert = Alert(type)
        alert.title = title
        alert.contentText = content
        return alert.showAndWait()
    }
}

