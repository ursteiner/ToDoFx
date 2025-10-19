package com.github.ursteiner.todofx.model

data class DbConnection (
    var url: String,
    var driver: String,
    var user: String,
    var password: String
)