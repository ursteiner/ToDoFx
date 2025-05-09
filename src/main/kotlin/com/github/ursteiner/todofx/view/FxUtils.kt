package com.github.ursteiner.todofx.view

import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.Tooltip
import java.util.Optional

object FxUtils {
    fun addToolTipToNode(node: Node, tooltipText: String){
        val tooltip = Tooltip(tooltipText)
        Tooltip.install(node, tooltip)
    }

    fun createMessageDialog(aTitle: String, content: String, type: Alert.AlertType = Alert.AlertType.INFORMATION): Optional<ButtonType>{
        val alert = Alert(type)
        with(alert) {
            title = aTitle
            contentText = content
            return showAndWait()
        }
    }
}