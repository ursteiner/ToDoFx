package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.model.NaiveBayesModelData
import kotlinx.serialization.json.Json
import kotlin.math.ln

class NaiveBayesClassification {

    private val categoryToTasks = mutableMapOf<String, MutableList<String>>()
    private val wordCounts = mutableMapOf<String, MutableMap<String, Int>>()
    private val categoryCounts = mutableMapOf<String, Int>()
    private val vocabulary = mutableSetOf<String>()
    private var totalTasks = 0

    fun exportModel() : String {
        val modelData = NaiveBayesModelData(
            wordCounts,
            categoryCounts,
            vocabulary,
            totalTasks
        )
        val json = Json.encodeToString(modelData)
        return json
    }

    fun importModel(json: String) {
        val modelData = Json.decodeFromString<NaiveBayesModelData>(json)
        wordCounts.clear(); wordCounts.putAll(modelData.wordCounts.mapValues { it.value.toMutableMap() })
        categoryCounts.clear(); categoryCounts.putAll(modelData.categoryCounts)
        vocabulary.clear(); vocabulary.addAll(modelData.vocabulary)
        totalTasks = modelData.totalTasks
    }

    fun train(task: String, category: String) {
        val words = preprocess(task)
        categoryToTasks.getOrPut(category) { mutableListOf() }.add(task)
        categoryCounts[category] = categoryCounts.getOrDefault(category, 0) + 1
        totalTasks++

        val counts = wordCounts.getOrPut(category) { mutableMapOf() }
        for (word in words) {
            counts[word] = counts.getOrDefault(word, 0) + 1
            vocabulary.add(word)
        }
    }

    fun predict(task: String): String? {
        val words = preprocess(task)
        if (words.isEmpty()){
            return null
        }

        var bestCategory: String? = null
        var bestLogProb = Double.NEGATIVE_INFINITY

        for (category in categoryCounts.keys) {
            val logProb = logProbability(words, category)
            if (logProb > bestLogProb) {
                bestLogProb = logProb
                bestCategory = category
            }
        }
        return bestCategory
    }

    private fun logProbability(words: List<String>, category: String): Double {
        val categoryPrior = categoryCounts[category]?.toDouble()?.div(totalTasks) ?: 0.0
        var logProb = ln(categoryPrior)

        val counts = wordCounts[category] ?: emptyMap()
        val totalWordsInCategory = counts.values.sum()
        val vocabSize = vocabulary.size

        for (word in words) {
            val wordCount = counts.getOrDefault(word, 0)
            // Laplace smoothing
            val wordProb = (wordCount + 1).toDouble() / (totalWordsInCategory + vocabSize)
            logProb += ln(wordProb)
        }
        return logProb
    }

    private fun preprocess(text: String): List<String> {
        return text
            .lowercase()
            .replace(Regex("[^a-z0-9 ]"), "")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
    }
}