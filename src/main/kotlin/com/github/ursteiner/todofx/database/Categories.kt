package com.github.ursteiner.todofx.database

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Categories : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", length = 100)

    override val primaryKey = PrimaryKey(id, name = "PK_Category_ID")
}