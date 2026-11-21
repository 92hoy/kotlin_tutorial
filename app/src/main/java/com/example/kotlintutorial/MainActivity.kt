package com.example.kotlintutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.kotlintutorial.ui.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

/** 앱의 유일한 Activity. Navigation 호스트를 띄우는 역할만 한다. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /** Compose 콘텐츠를 설정한다. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavHost()
            }
        }
    }
}
