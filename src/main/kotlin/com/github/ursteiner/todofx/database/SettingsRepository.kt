package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.constants.AppSettings
import com.github.ursteiner.todofx.model.Setting

interface SettingsRepository {
    fun getSettings(): List<Setting>
    fun updateSetting(setting: AppSettings, value: String)
}