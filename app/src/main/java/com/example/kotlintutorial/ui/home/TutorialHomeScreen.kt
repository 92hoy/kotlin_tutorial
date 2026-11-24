package com.example.kotlintutorial.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kotlintutorial.ui.topic.containerColor
import com.example.kotlintutorial.ui.tutorial.Level
import com.example.kotlintutorial.ui.tutorial.Topic

/**
 * Kotlin Tutorial 홈 화면. 기능을 난이도(초급, 중급, 고급, 종합 예제)별로 묶어 보여 준다.
 * 맨 위 카드로 앱 구조 화면에도 들어갈 수 있다.
 *
 * Kotlin 포인트: 이동 동작을 함수 타입(`(Topic) -> Unit`, `() -> Unit`)으로 받아서
 * 화면이 NavController를 몰라도 되게 했다. Java의 리스너 인터페이스를 대신함.
 *
 * @param topics 보여 줄 기능 전체 목록.
 * @param onOpenTopic 기능 카드를 눌렀을 때 호출된다.
 * @param onOpenArchitecture 앱 구조 카드를 눌렀을 때 호출된다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialHomeScreen(
    topics: List<Topic>,
    onOpenTopic: (Topic) -> Unit,
    onOpenArchitecture: () -> Unit,
) {
    // 난이도별로 묶는다. groupBy는 컬렉션 연산 튜토리얼에서 다루는 함수 그대로임.
    val grouped = topics.groupBy { it.level }

    Scaffold(topBar = { TopAppBar(title = { Text("Kotlin Tutorial") }) }) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text(
                    "Java로 안드로이드를 만들어 온 개발자를 위한 Kotlin 기능 정리. " +
                        "설명과 예시 코드를 읽고 직접 실행해 볼 수 있다.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            item { ArchitectureCard(onClick = onOpenArchitecture) }

            Level.entries.forEach { level ->
                val levelTopics = grouped[level].orEmpty()
                if (levelTopics.isNotEmpty()) {
                    item(key = "header-" + level.name) { LevelHeader(level, levelTopics.size) }
                    items(levelTopics, key = { it.id }) { topic ->
                        TopicCard(topic = topic, onClick = { onOpenTopic(topic) })
                    }
                }
            }
        }
    }
}

/**
 * 앱 구조 화면으로 가는 카드.
 *
 * @param onClick 카드를 눌렀을 때 호출된다.
 */
@Composable
private fun ArchitectureCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseSurface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "앱 구조 한눈에 보기",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.inverseOnSurface,
            )
            Text(
                "아키텍처, 디자인 패턴, 디렉토리 위치",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.inverseOnSurface,
            )
        }
    }
}

/**
 * 난이도 구역의 머리글. 난이도 배지, 설명, 기능 개수를 한 줄에 보여 준다.
 *
 * @param level 난이도.
 * @param count 이 난이도에 속한 기능 개수.
 */
@Composable
private fun LevelHeader(level: Level, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(shape = RoundedCornerShape(50), color = level.containerColor()) {
            Text(
                text = level.label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            )
        }
        Text(
            text = level.description + " · " + count + "개",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

/**
 * 기능 한 개를 나타내는 카드. 제목과 한 줄 설명을 보여 주고 누르면 상세 화면으로 간다.
 *
 * @param topic 보여 줄 기능.
 * @param onClick 카드를 눌렀을 때 호출된다.
 */
@Composable
private fun TopicCard(topic: Topic, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(topic.title, style = MaterialTheme.typography.titleMedium)
            Text(topic.summary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
