package com.github.ursteiner.todofx.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

class Task {
    private val name: SimpleStringProperty = SimpleStringProperty("")
    private val date: SimpleStringProperty = SimpleStringProperty("")
    private val id: SimpleIntegerProperty = SimpleIntegerProperty()
    private val isDone: SimpleBooleanProperty = SimpleBooleanProperty(false)
    private val isDoneIcon: SimpleStringProperty = SimpleStringProperty("")

    constructor(name: String, date: String, id: Int = 0, isDone: Boolean = false){
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

    fun setIsDoneIconProperty(isTaskDoneIcon: String){
        isDoneIcon.set(isTaskDoneIcon)
    }

    fun getIsDoneIconProperty(): String{
        return isDoneIcon.get()
    }
}