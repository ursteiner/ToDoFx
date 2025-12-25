package com.github.ursteiner.todofx.viewModel

import com.github.ursteiner.todofx.constants.AppSettings
import com.github.ursteiner.todofx.model.Setting
import com.github.ursteiner.todofx.database.SettingsDatabaseService
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class SettingsViewModel(
    private val settingsDb: SettingsDatabaseService
) {

    val settings: ObservableList<Setting> = FXCollections.observableArrayList()

    fun loadSettings() {
        settings.setAll(settingsDb.getSettings())
    }

    fun updateSetting(setting: AppSettings, value: String){
        settingsDb.updateSetting(setting, value)
        loadSettings()
    }

    fun getSetting(setting: AppSettings): String?{
        return settings.firstOrNull { it.name == setting.key }?.value
    }
}