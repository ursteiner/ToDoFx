package com.github.ursteiner.todofx

import javafx.beans.property.SimpleStringProperty

class Task {
    var name: SimpleStringProperty = SimpleStringProperty("")
    var date: SimpleStringProperty = SimpleStringProperty("")

    constructor(name: String, date: String){
        setNameProperty(name)
        setDateProperty(date)
    }

    fun setNameProperty(taskName: String){
        name.set(taskName)
    }

    fun getNameProperty(): String{
        return name.get()
    }

    fun setDateProperty(taskDate: String){
        date.set(taskDate)
    }

    fun getDateProperty(): String{
        return date.get()
    }
}