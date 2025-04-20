package com.github.ursteiner.todofx.view

import javafx.scene.control.Alert

object FxMessageDialog {

    fun createMessageDialog(title: String, content: String, type: Alert.AlertType = Alert.AlertType.INFORMATION){
        val alert = Alert(type)
        alert.title = title
        alert.contentText = content
        alert.showAndWait()
    }
}

