package com.github.ursteiner.todofx.view

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import java.util.Optional

object FxMessageDialog {

    fun createMessageDialog(aTitle: String, content: String, type: Alert.AlertType = Alert.AlertType.INFORMATION): Optional<ButtonType>{
        val alert = Alert(type)
        with(alert) {
            title = aTitle
            contentText = content
            return showAndWait()
        }
    }
}

