package com.example.kotlintutorial.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlintutorial.ui.navigation.DemoEntry

/**
 * Kotlin 기능 카탈로그 홈 화면. 데모 목록을 카드로 보여 주고 누르면 해당 데모로 이동한다.
 *
 * Kotlin 포인트: 이동 동작을 `(DemoEntry) -> Unit` 람다로 받아서
 * 화면이 NavController를 몰라도 되게 했다. Java의 리스너 인터페이스 대신 함수 타입을 씀.
 *
 * @param entries 보여 줄 데모 목록.
 * @param onOpen 카드를 눌렀을 때 호출된다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogHomeScreen(entries: List<DemoEntry>, onOpen: (DemoEntry) -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Kotlin 기능 카탈로그") }) }) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(entries, key = { it.route }) { entry ->
                Card(modifier = Modifier.fillMaxWidth().clickable { onOpen(entry) }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(entry.title, style = MaterialTheme.typography.titleMedium)
                        Text(entry.summary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
