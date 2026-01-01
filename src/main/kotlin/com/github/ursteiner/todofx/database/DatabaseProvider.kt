package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.model.DbConnection
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseProvider {
    fun connect(dbConnection: DbConnection) {
        Database.connect(dbConnection.url, dbConnection.driver, dbConnection.user, dbConnection.password)
        transaction {
            addLogger(StdOutSqlLogger)
        }
    }
}