package org.bibletranslationtools.common.data

import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException

class ResourceTypeTest {

    @Test
    fun createValidResourceType() {
        Assert.assertEquals(ResourceType.of("ulb"), ResourceType.ULB)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidResourceTypeThrowsException() {
        ResourceType.of("wrong")
    }
}
