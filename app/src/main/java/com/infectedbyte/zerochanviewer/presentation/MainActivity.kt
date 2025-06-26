package com.infectedbyte.zerochanviewer.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets.ZeroImageListScreen
import com.infectedbyte.zerochanviewer.presentation.imageDetail.componets.ZeroImageScreen
import com.infectedbyte.zerochanviewer.presentation.ui.theme.ZeroChanViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeroChanViewerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController,
                        startDestination = Screen.BrowserScreen
                    ) {
                        composable<Screen.BrowserScreen> {
                            ZeroImageListScreen(navController)
                        }
                        composable<Screen.ZeroImageScreenRoute> {
                            ZeroImageScreen()
                        }

                    }
                }

            }
        }
    }
}
