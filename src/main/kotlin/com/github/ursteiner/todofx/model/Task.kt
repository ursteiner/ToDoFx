package com.github.ursteiner.todofx.model

data class Task (
    var name: String,
    var date: String,
    var id: Int,
    var isDone: Boolean,
    var resolvedDate: String,
    var category: String?
)
