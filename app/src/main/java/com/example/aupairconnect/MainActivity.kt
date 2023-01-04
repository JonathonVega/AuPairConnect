package com.example.aupairconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.aupairconnect.graphs.RootNavigationGraph
import com.example.aupairconnect.ui.theme.AuPairConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuPairConnectTheme {
                RootNavigationGraph(navController = rememberNavController())
            }
        }
    }
}

