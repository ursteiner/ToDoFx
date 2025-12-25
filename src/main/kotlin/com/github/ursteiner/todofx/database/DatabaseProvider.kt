package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.model.DbConnection
import org.jetbrains.exposed.v1.jdbc.Database

object DatabaseProvider {
    fun connect(dbConnection: DbConnection) {
        Database.connect(dbConnection.url, dbConnection.driver, dbConnection.user, dbConnection.password)
    }
}