package com.campuscrave.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.campuscrave.app.ui.navigation.CampusCraveNavGraph
import com.campuscrave.app.ui.theme.CampusCraveTheme
import com.campuscrave.app.ui.theme.CanvasBackground
import com.campuscrave.app.ui.viewmodel.CampusCraveViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CampusCraveViewModel by viewModels {
        val app = application as CampusCraveApplication
        CampusCraveViewModel.Factory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CampusCraveTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CanvasBackground
                ) {
                    CampusCraveNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}

