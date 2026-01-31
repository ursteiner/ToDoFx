package com.github.ursteiner.todofx.viewModel

import com.github.ursteiner.todofx.database.NaiveBayesRepository
import com.github.ursteiner.todofx.model.Task
import com.github.ursteiner.todofx.service.NaiveBayesClassification
import org.slf4j.LoggerFactory

class ClassificationViewModel(
    private val classificationDb: NaiveBayesRepository
) {
    private val logger = LoggerFactory.getLogger(NaiveBayesClassification::class.java)
    private val PERFORMANCE_LOG_PREFIX = "Performance: "
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
        val start = System.currentTimeMillis()
        val taskClassification = NaiveBayesClassification()

        tasks.forEach {
            taskClassification.train(it.name, it.category ?: "")
        }

        updateClassificationModel(taskClassification.exportModel())
        logger.info("$PERFORMANCE_LOG_PREFIX Model train took ${System.currentTimeMillis() - start} ms")
    }
}