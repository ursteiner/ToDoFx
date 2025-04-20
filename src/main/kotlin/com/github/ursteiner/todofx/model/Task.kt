package com.github.ursteiner.todofx.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

class Task {
    val name: SimpleStringProperty = SimpleStringProperty("")
    val date: SimpleStringProperty = SimpleStringProperty("")
    val id: SimpleIntegerProperty = SimpleIntegerProperty()
    val isDone: SimpleBooleanProperty = SimpleBooleanProperty(false)

    constructor(name: String, date: String, id: Int, isDone: Boolean = false){
        setNameProperty(name)
        setDateProperty(date)
        setIdProperty(id)
        setIsDoneProperty(isDone)
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

    fun setIsDoneProperty(isTaskDone: Boolean){
        isDone.set(isTaskDone)
    }

    fun getIsDoneProperty(): Boolean{
        return isDone.get()
    }
}