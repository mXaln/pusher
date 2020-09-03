package org.bibletranslationtools.common.data

import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException

class GroupingTest {

    @Test
    fun createValidGrouping() {
        Assert.assertEquals(Grouping.of("chapter"), Grouping.CHAPTER)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidGroupingThrowsException() {
        Grouping.of("wrong")
    }
}
