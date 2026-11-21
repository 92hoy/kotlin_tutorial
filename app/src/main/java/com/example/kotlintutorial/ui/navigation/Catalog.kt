package com.example.kotlintutorial.ui.navigation

import androidx.compose.runtime.Composable
import com.example.kotlintutorial.ui.MemoScreen
import com.example.kotlintutorial.ui.demo.NullSafetyDemo

/**
 * 카탈로그에 등록되는 데모 한 개의 정보.
 *
 * Kotlin 포인트: 화면 자체를 `@Composable () -> Unit` 함수 타입 프로퍼티로 들고 있다.
 * Java라면 Fragment 클래스나 인터페이스 구현체를 따로 만들어야 하는 자리임.
 *
 * @property route Navigation 목적지 이름. 데모마다 겹치면 안 된다.
 * @property title 홈 목록에 보이는 제목.
 * @property summary 어떤 Kotlin 기능을 다루는지 한 줄 설명.
 * @property screen 데모 화면을 그리는 Composable.
 */
data class DemoEntry(
    val route: String,
    val title: String,
    val summary: String,
    val screen: @Composable () -> Unit,
)

/**
 * 홈 화면에 나오는 데모 목록.
 * 새 데모를 만들면 여기에 [DemoEntry] 한 줄만 추가하면 홈 목록과 Navigation에 같이 반영된다.
 */
val catalog: List<DemoEntry> = listOf(
    DemoEntry(
        route = "memo",
        title = "메모 앱",
        summary = "data class, suspend, Flow, sealed 상태를 한 번에 쓴 기본 뼈대",
        screen = { MemoScreen() },
    ),
    DemoEntry(
        route = "null-safety",
        title = "null 안전성",
        summary = "`?.`, `?:`, `!!`, `let`을 같은 입력으로 비교",
        screen = { NullSafetyDemo() },
    ),
)
