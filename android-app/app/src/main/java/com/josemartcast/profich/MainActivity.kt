package com.josemartcast.profich

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.josemartcast.profich.presentation.ProFichApp
import com.josemartcast.profich.presentation.theme.ProFichTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProFichTheme {
                ProFichApp()
            }
        }
    }
}
