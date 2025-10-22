package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.constants.AppSettings
import com.github.ursteiner.todofx.model.DbConnection
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.slf4j.LoggerFactory

class SettingsDatabaseServiceImpl : SettingsDatabaseService {

    private val logger = LoggerFactory.getLogger(SettingsDatabaseServiceImpl::class.java)

    private constructor(dbConnection: DbConnection) {
        logger.info("Set database connection to ${dbConnection.url}")
        Database.connect(dbConnection.url, dbConnection.driver, dbConnection.user, dbConnection.password)
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Settings)
        }
    }

    companion object {

        @Volatile
        private var instance: SettingsDatabaseServiceImpl? = null

        fun getInstance(dbConnection: DbConnection) =
            instance ?: synchronized(this) {
                instance ?: SettingsDatabaseServiceImpl(dbConnection).also { instance = it }
            }
    }

    override fun getSetting(setting: AppSettings): String? {
        logger.info("get setting <${setting.key}>")
        var resultValue: String? = null

        transaction {
            addLogger(StdOutSqlLogger)
            val result = Settings.selectAll().where {
                Settings.key eq setting.key
            }

            result.forEach {
                resultValue = it[Settings.value]
            }
        }

        return resultValue
    }

    private fun addSetting(setting: AppSettings, value: String) {
        logger.info("add setting <${setting.key}>")

        transaction {
            addLogger(StdOutSqlLogger)
            Settings.insert {
                it[key] = setting.key
                it[Settings.value] = value
            }
        }
    }

    override fun updateSetting(setting: AppSettings, value: String) {
        logger.info("update setting <${setting.key}>")
        var updated = 0

        transaction {
            addLogger(StdOutSqlLogger)
            updated = Settings.update({ Settings.key eq setting.key }) {
                it[Settings.value] = value
            }
        }

        logger.info("$updated setting(s) updated")

        if (updated == 0) {
            addSetting(setting, value)
        }
    }
}