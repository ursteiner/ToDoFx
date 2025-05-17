package com.github.ursteiner.todofx.database

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

object Tasks : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", length = 500)
    val date: Column<String> = varchar("date", length = 30)
    val isDone: Column<Boolean> = bool("isDone")
    val resolvedDate: Column<String?> = varchar("resolvedDate", length = 30).nullable()
    val categoryId: Column<Int?> = integer("categoryId").references(Categories.id).nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_Task_ID")
}
