package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.constants.AppSettings
import com.github.ursteiner.todofx.model.Setting
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.slf4j.LoggerFactory

class SettingsRepositoryImpl : SettingsRepository {

    private val logger = LoggerFactory.getLogger(SettingsRepositoryImpl::class.java)

    init {
        transaction {
            SchemaUtils.create(Settings)
        }
    }

    override fun getSettings(): List<Setting> = transaction {
        logger.info("get settings")

        Settings.selectAll().map {
            Setting(
                it[Settings.key],
                it[Settings.value]
            )
        }
    }

    private fun addSetting(setting: AppSettings, value: String) {
        logger.info("add setting ${setting.key} = $value")

        transaction {
            Settings.insert {
                it[key] = setting.key
                it[Settings.value] = value
            }
        }
    }

    override fun updateSetting(setting: AppSettings, value: String) {
        logger.info("update setting ${setting.key} = $value")

        var updated = 0

        transaction {
            updated = Settings.update({ Settings.key eq setting.key }) {
                it[Settings.value] = value
            }
        }

        if (updated == 0) {
            addSetting(setting, value)
        }
    }
}