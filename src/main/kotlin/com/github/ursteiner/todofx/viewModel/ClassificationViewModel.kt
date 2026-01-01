package com.github.ursteiner.todofx.viewModel

import com.github.ursteiner.todofx.database.NaiveBayesRepository
import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.service.NaiveBayesClassification

class ClassificationViewModel(
    private val classificationDb: NaiveBayesRepository
) {
    val classification = NaiveBayesClassification()

    fun loadClassificationModel() {
        val modelData = classificationDb.getModel()
        if(modelData != null){
            classification.importModel(modelData)
        }
    }

    fun predictCategory(text: String): String? {
        return classification.predict(text)
    }

    fun updateClassificationModel(model: String) {
        classificationDb.updateModel(model)
    }

    fun getModelDate(): String? {
        return classificationDb.getModelDate()?.substring(0, 10)
    }

    fun trainNewModel(tasks: MutableList<Task>) {
        val taskClassification = NaiveBayesClassification()

        tasks.forEach {
            taskClassification.train(it.name, it.category ?: "")
        }

        updateClassificationModel(taskClassification.exportModel())
    }
}