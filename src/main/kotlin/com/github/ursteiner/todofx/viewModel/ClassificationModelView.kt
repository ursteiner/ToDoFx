package com.github.ursteiner.todofx.viewModel

import com.github.ursteiner.todofx.database.NaiveBayesModelService
import com.github.ursteiner.todofx.service.NaiveBayesClassification

class ClassificationModelView(
    private val classificationDb: NaiveBayesModelService
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
}