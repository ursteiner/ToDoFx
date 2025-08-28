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
    private val resolvedDate: SimpleStringProperty = SimpleStringProperty("")
    private val category: SimpleStringProperty = SimpleStringProperty("")

    constructor(name: String, date: String, categoryName: String?, id: Int = 0, isDone: Boolean = false, resolvedDate: String = ""){
        setNameProperty(name)
        setDateProperty(date)
        setIdProperty(id)
        setIsDoneProperty(isDone)
        setResolvedDate(resolvedDate)
        setCategoryProperty(categoryName ?: "")
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

    @Suppress("unused")
    fun getIsDoneIconProperty(): String{
        return isDoneIcon.get()
    }

    fun setResolvedDate(resolvedDate: String){
        this.resolvedDate.set(resolvedDate)
    }

    fun getResolvedDateProperty(): String{
        return this.resolvedDate.get()
    }

    fun setCategoryProperty(categoryName: String){
        this.category.set(categoryName)
    }

    fun getCategoryProperty(): String{
        return this.category.get()
    }
}