package com.github.ursteiner.todofx.database

import com.github.ursteiner.todofx.constants.AppSettings

interface SettingsDatabaseService {
    fun getSetting(setting: AppSettings): String?
    fun updateSetting(setting: AppSettings, value: String)
}