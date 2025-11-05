package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.model.DbConnection
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter

class NaiveBayesModelServiceImpl : NaiveBayesModelService {

    private val logger = LoggerFactory.getLogger(NaiveBayesModelServiceImpl::class.java)
    private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    private constructor(dbConnection: DbConnection) {
        logger.info("Set database connection to ${dbConnection.url}")
        Database.connect(dbConnection.url, dbConnection.driver, dbConnection.user, dbConnection.password)
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(NaiveBaseModel)
        }
    }

    companion object {

        @Volatile
        private var instance: NaiveBayesModelServiceImpl? = null

        fun getInstance(dbConnection: DbConnection) =
            instance ?: synchronized(this) {
                instance ?: NaiveBayesModelServiceImpl(dbConnection).also { instance = it }
            }
    }

    override fun getModelDate(): String? {
        logger.info("Get model creation date")
        return getModelValue(NaiveBaseModel.creationDate)
    }

    override fun getModel(): String? {
        logger.info("Get model")
        return getModelValue(NaiveBaseModel.data)
    }

    private fun getModelValue(column: Column<String>): String? {
        var columnValue: String? = null

        transaction {
            addLogger(StdOutSqlLogger)
            val result = NaiveBaseModel.selectAll()

            result.forEach {
                columnValue = it[column]
            }
        }

        return columnValue
    }

    override fun updateModel(model: String) {
        logger.info("Update model")

        transaction {
            NaiveBaseModel.deleteAll()
            NaiveBaseModel.insert {
                it[data] = model
                it[creationDate] = java.time.LocalDateTime.now().format(dateTimeFormat)
            }
        }
    }
}