package com.github.ursteiner.todofx

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

class Task {
    var name: SimpleStringProperty = SimpleStringProperty("")
    var date: SimpleStringProperty = SimpleStringProperty("")
    var id: SimpleIntegerProperty = SimpleIntegerProperty()

    constructor(name: String, date: String, id: Int){
        setNameProperty(name)
        setDateProperty(date)
        setIdProperty(id)
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

    fun setIdProperty(taskId: Int){
        id.set(taskId)
    }

    fun getIdProperty(): Int{
        return id.get()
    }
}