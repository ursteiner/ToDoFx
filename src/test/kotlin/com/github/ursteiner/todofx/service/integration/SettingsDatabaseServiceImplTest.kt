package com.github.ursteiner.todofx.service.integration

import com.github.ursteiner.todofx.constants.AppSettings
import org.junit.jupiter.api.Assertions.*
import com.github.ursteiner.todofx.database.SettingsDatabaseServiceImpl
import com.github.ursteiner.todofx.model.DbConnection
import org.junit.jupiter.api.Test

class SettingsDatabaseServiceImplTest {
    private val databaseConnection = DbConnection("jdbc:h2:mem:testTasks;DB_CLOSE_DELAY=-1;", "org.h2.Driver", "root", "")
    private val testCandidate = SettingsDatabaseServiceImpl.getInstance(databaseConnection)

    @Test
    fun testStoreAndRetrieveSetting(){
        testCandidate.updateSetting(AppSettings.LANGUAGE, "test")
        assertEquals("test", testCandidate.getSetting(AppSettings.LANGUAGE))
    }
}