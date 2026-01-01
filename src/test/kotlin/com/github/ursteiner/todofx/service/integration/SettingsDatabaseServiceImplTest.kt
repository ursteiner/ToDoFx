package com.github.ursteiner.todofx.service.integration

import com.github.ursteiner.todofx.constants.AppSettings
import com.github.ursteiner.todofx.database.DatabaseProvider
import org.junit.jupiter.api.Assertions.*
import com.github.ursteiner.todofx.database.SettingsRepositoryImpl
import com.github.ursteiner.todofx.model.DbConnection
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class SettingsDatabaseServiceImplTest {
    private val testCandidate = SettingsRepositoryImpl()

    @Test
    fun testStoreAndRetrieveSetting(){
        testCandidate.updateSetting(AppSettings.LANGUAGE, "test")
        val result = testCandidate.getSettings().firstOrNull { it.name == AppSettings.LANGUAGE.key }?.value

        assertEquals("test", result)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setupDatabaseConnection() {
            val databaseTestConnection = DbConnection("jdbc:h2:mem:testTasks;DB_CLOSE_DELAY=-1;", "org.h2.Driver", "root", "")
            DatabaseProvider.connect(databaseTestConnection)
        }
    }
}