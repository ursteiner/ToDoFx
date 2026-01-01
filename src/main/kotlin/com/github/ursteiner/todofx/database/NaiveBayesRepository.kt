package com.github.ursteiner.todofx.database

interface NaiveBayesRepository {
    fun getModel(): String?
    fun getModelDate(): String?
    fun updateModel(model: String)
}