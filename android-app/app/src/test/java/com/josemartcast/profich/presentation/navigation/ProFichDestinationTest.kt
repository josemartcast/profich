package com.josemartcast.profich.presentation.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ProFichDestinationTest {
    @Test
    fun `home is the start destination`() {
        assertEquals(ProFichDestination.Home.route, ProFichDestination.start.route)
    }

    @Test
    fun `destinations have distinct routes`() {
        assertNotEquals(ProFichDestination.Home.route, ProFichDestination.About.route)
    }
}
