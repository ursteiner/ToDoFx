package com.github.ursteiner.todofx.database

interface NaiveBayesModelService {
    fun getModel(): String?
    fun getModelDate(): String?
    fun updateModel(model: String)
}