package com.github.ursteiner.todofx.service

import com.github.ursteiner.todofx.constants.AvailableLanguages
import com.github.ursteiner.todofx.constants.TranslationKeys
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.enums.enumEntries

class LanguageServiceImplTest {

    val testCandidate = LanguageServiceImpl(AvailableLanguages.ENGLISH)

    @Test
    fun testGetFirstSelectTaskInTableEnglish(){
        Assertions.assertEquals(
            "Please first select a task in the table!",
            testCandidate.getTranslationForKey(
            TranslationKeys.FIRST_SELECT_TASK_IN_TABLE))
    }

    @Test
    fun testAllKeysAreTranslated(){
        //In case there is no translation, the key is returned.
        //So if the key is returned we are missing a translation.
        enumEntries<TranslationKeys>().forEach {
            Assertions.assertNotEquals(it.name, testCandidate.getTranslationForKey(it))
        }
    }
}