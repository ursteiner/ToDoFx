package com.github.ursteiner.todofx.database

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter

class NaiveBayesRepositoryImpl : NaiveBayesRepository {

    private val logger = LoggerFactory.getLogger(NaiveBayesRepositoryImpl::class.java)
    private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    init {
        transaction {
           SchemaUtils.create(NaiveBayesModel)
        }
    }

    override fun getModelDate(): String? {
        logger.info("Get model creation date")
        return getModelValue(NaiveBayesModel.creationDate)
    }

    override fun getModel(): String? {
        logger.info("Get model")
        return getModelValue(NaiveBayesModel.data)
    }

    private fun getModelValue(column: Column<String>): String? {
        var columnValue: String? = null

        transaction {
            val result = NaiveBayesModel.selectAll()

            result.forEach {
                columnValue = it[column]
            }
        }

        return columnValue
    }

    override fun updateModel(model: String) {
        logger.info("Update model")

        transaction {
            NaiveBayesModel.deleteAll()
            NaiveBayesModel.insert {
                it[data] = model
                it[creationDate] = java.time.LocalDateTime.now().format(dateTimeFormat)
            }
        }
    }
}