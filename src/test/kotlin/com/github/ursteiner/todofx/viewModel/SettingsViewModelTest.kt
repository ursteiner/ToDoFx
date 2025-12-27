package com.github.ursteiner.todofx.viewModel

import com.github.ursteiner.todofx.constants.AppSettings
import com.github.ursteiner.todofx.database.SettingsDatabaseService
import com.github.ursteiner.todofx.model.Setting
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class SettingsViewModelTest {

    private lateinit var settingsDb: SettingsDatabaseService
    private lateinit var viewModel: SettingsViewModel

    @BeforeEach
    fun setUp() {
        settingsDb = mock(SettingsDatabaseService::class.java)
        viewModel = SettingsViewModel(settingsDb)
    }

    @Test
    fun shouldContainSetting_when_successfullyLoaded() {
        val mockSettings = listOf(Setting("theme", "dark"))
        `when`(settingsDb.getSettings()).thenReturn(mockSettings)

        viewModel.loadSettings()

        assertEquals(1, viewModel.settings.size)
        assertEquals("theme", viewModel.settings.first().name)
        assertEquals("dark", viewModel.settings.first().value)
    }

    @Test
    fun shouldContainSetting_when_successfullyUpdated() {
        val setting = AppSettings.LANGUAGE
        val value = "en"
        `when`(settingsDb.getSettings()).thenReturn(listOf(Setting(setting.key, value)))

        viewModel.updateSetting(setting, value)

        verify(settingsDb).updateSetting(setting, value)
        assertEquals(value, viewModel.getSetting(setting))
    }

    @Test
    fun shouldContainSetting_when_successfullyAdded() {
        viewModel.settings.add(Setting(AppSettings.LANGUAGE.key, "en"))
        val result = viewModel.getSetting(AppSettings.LANGUAGE)
        assertEquals("en", result)
    }

    @Test
    fun shouldReturnNull_when_settingNotPresent() {
        val result = viewModel.getSetting(AppSettings.LANGUAGE)
        assertNull(result)
    }
}