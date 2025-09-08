package com.github.ursteiner.todofx.service.integration

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.service.LanguageServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.enums.enumEntries

class LanguageServiceImplTest {

    private val testCandidate = LanguageServiceImpl.Companion.getInstance("en")

    @Test
    fun testGetFirstSelectTaskInTableEnglish(){
        Assertions.assertEquals(
            "Please first select a task in the table!",
            testCandidate.getTranslationForKey(
                TranslationKeys.FIRST_SELECT_TASK_IN_TABLE
            )
        )
    }

    @Test
    fun testAllKeysAreTranslated(){
        //In case there is no translation, the key is returned.
        //So if the key is returned we are missing a translation.
        enumEntries<TranslationKeys>().forEach {
            Assertions.assertNotEquals(
                it.name,
                testCandidate.getTranslationForKey(it)
            )
        }
    }

    @Test
    fun testEnumAndJsonFileHaveSameAmountOfProperties(){
        Assertions.assertEquals(
            enumEntries<TranslationKeys>().size,
            testCandidate.getAmountOfTranslations()
        )
    }
}