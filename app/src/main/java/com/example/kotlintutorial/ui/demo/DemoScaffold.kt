package com.example.kotlintutorial.ui.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 데모 화면들이 공통으로 쓰는 틀. 상단 제목과 세로 스크롤 영역을 제공한다.
 *
 * Kotlin 포인트: 내용을 `ColumnScope.() -> Unit` 람다로 받아서
 * 호출하는 쪽이 `Column { }` 안에 있는 것처럼 바로 UI를 나열할 수 있다(수신 객체 람다).
 *
 * @param title 상단 바 제목.
 * @param content 화면 본문.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScaffold(title: String, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content,
        )
    }
}
