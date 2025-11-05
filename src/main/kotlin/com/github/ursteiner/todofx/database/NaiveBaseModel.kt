package com.github.ursteiner.todofx.database

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

object NaiveBaseModel : Table() {
    val creationDate: Column<String> = varchar("creationDate", length = 30)
    val data: Column<String> = text("data")
}