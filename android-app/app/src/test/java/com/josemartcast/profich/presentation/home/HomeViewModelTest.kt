package com.josemartcast.profich.presentation.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeViewModelTest {
    @Test
    fun `initial state identifies the project foundation`() {
        val state = HomeViewModel().uiState.value

        assertEquals("ProFich", state.title)
        assertEquals("Base del proyecto preparada", state.status)
    }
}
