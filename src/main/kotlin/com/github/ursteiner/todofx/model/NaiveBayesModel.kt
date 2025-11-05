package com.github.ursteiner.todofx.model

import kotlinx.serialization.Serializable

@Serializable
data class NaiveBayesModelData(
    val wordCounts: Map<String, Map<String, Int>>,
    val categoryCounts: Map<String, Int>,
    val vocabulary: Set<String>,
    val totalTasks: Int
)
