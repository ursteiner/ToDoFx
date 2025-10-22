package com.github.ursteiner.todofx.database

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

object Settings : Table() {
    val key: Column<String> = varchar("key", length = 100).uniqueIndex()
    val value: Column<String> = varchar("value", length = 100)
}