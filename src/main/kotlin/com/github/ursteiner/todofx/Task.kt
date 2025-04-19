package com.github.ursteiner.todofx

import javafx.beans.property.SimpleStringProperty

class Task {
    var name: SimpleStringProperty = SimpleStringProperty("")

    constructor(name: String){
        setName(name)
    }

    fun setName(taskName: String){
        name.set(taskName)
    }

    fun getName(): String{
        return name.get()
    }
}