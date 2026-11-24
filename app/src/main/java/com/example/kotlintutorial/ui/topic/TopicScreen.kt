package com.example.kotlintutorial.ui.topic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kotlintutorial.ui.tutorial.Level
import com.example.kotlintutorial.ui.tutorial.Topic

/**
 * 난이도별 배경색. 홈 화면과 상세 화면이 같은 색으로 난이도를 구분하게 한다.
 *
 * Kotlin 포인트: `when`을 식으로 써서 enum의 모든 값을 빠짐없이 처리한다(else 불필요).
 * 확장 함수라서 `level.containerColor()`처럼 enum에 메서드가 있는 것처럼 호출함.
 */
@Composable
fun Level.containerColor(): Color = when (this) {
    Level.BEGINNER -> MaterialTheme.colorScheme.primaryContainer
    Level.INTERMEDIATE -> MaterialTheme.colorScheme.secondaryContainer
    Level.ADVANCED -> MaterialTheme.colorScheme.tertiaryContainer
    Level.PROJECT -> MaterialTheme.colorScheme.surfaceVariant
}

/**
 * 기능 상세 화면. 설명, 핵심 포인트, Kotlin 예시 코드, Java 비교 코드를 위에서 아래로 보여 주고
 * 하단 버튼으로 실습 화면을 연다.
 *
 * @param topic 보여 줄 기능.
 * @param onBack 뒤로 가기 버튼을 눌렀을 때 호출된다.
 * @param onRunDemo 하단 "직접 해보기" 버튼을 눌렀을 때 호출된다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(topic: Topic, onBack: () -> Unit, onRunDemo: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topic.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Button(
                    onClick = onRunDemo,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                ) { Text("직접 해보기") }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LevelBadge(topic.level)

            SectionTitle("설명")
            Text(topic.explanation, style = MaterialTheme.typography.bodyLarge)

            SectionTitle("핵심 포인트")
            topic.points.forEach { point ->
                Row {
                    Text("•  ", style = MaterialTheme.typography.bodyLarge)
                    Text(point, style = MaterialTheme.typography.bodyLarge)
                }
            }

            SectionTitle("Kotlin 예시 코드")
            CodeBlock(topic.kotlinCode)

            // Java 비교 코드는 있을 때만 보여 준다.
            topic.javaCode?.let { java ->
                SectionTitle("Java로 쓰면")
                CodeBlock(java)
            }
        }
    }
}

/**
 * 난이도를 알약 모양으로 보여 주는 배지.
 *
 * @param level 표시할 난이도.
 */
@Composable
private fun LevelBadge(level: Level) {
    Surface(shape = RoundedCornerShape(50), color = level.containerColor()) {
        Text(
            text = level.label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        )
    }
}

/**
 * 상세 화면의 구역 제목.
 *
 * @param text 제목 글자.
 */
@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp),
    )
}
