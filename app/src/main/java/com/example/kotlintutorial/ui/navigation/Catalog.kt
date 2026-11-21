package com.example.kotlintutorial.ui.navigation

import androidx.compose.runtime.Composable
import com.example.kotlintutorial.ui.MemoScreen
import com.example.kotlintutorial.ui.demo.CollectionDemo
import com.example.kotlintutorial.ui.demo.CoroutineDemo
import com.example.kotlintutorial.ui.demo.DelegationDemo
import com.example.kotlintutorial.ui.demo.ExtensionDemo
import com.example.kotlintutorial.ui.demo.FlowSearchDemo
import com.example.kotlintutorial.ui.demo.ScopeFunctionDemo
import com.example.kotlintutorial.ui.demo.SealedStateDemo
import com.example.kotlintutorial.ui.demo.DataClassDemo
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
    DemoEntry(
        route = "data-class",
        title = "data class",
        summary = "`copy`, 구조 분해, `==`와 `===` 비교",
        screen = { DataClassDemo() },
    ),
    DemoEntry(
        route = "sealed-state",
        title = "sealed 상태",
        summary = "`sealed interface`와 else 없는 `when`",
        screen = { SealedStateDemo() },
    ),
    DemoEntry(
        route = "extension",
        title = "확장 함수",
        summary = "String, Int, List에 덧붙인 확장 함수와 확장 프로퍼티",
        screen = { ExtensionDemo() },
    ),
    DemoEntry(
        route = "scope-function",
        title = "스코프 함수",
        summary = "`let`, `run`, `with`, `apply`, `also`의 수신 방식과 반환값 비교",
        screen = { ScopeFunctionDemo() },
    ),
    DemoEntry(
        route = "collection",
        title = "컬렉션 연산",
        summary = "`map`, `filter`, `groupBy`, `partition`과 시퀀스 지연 평가",
        screen = { CollectionDemo() },
    ),
    DemoEntry(
        route = "coroutine",
        title = "코루틴",
        summary = "`async`/`await` 병렬 실행, 구조적 동시성, 취소",
        screen = { CoroutineDemo() },
    ),
    DemoEntry(
        route = "flow-search",
        title = "Flow 검색",
        summary = "`StateFlow`, `debounce`, `combine`으로 만든 검색",
        screen = { FlowSearchDemo() },
    ),
    DemoEntry(
        route = "delegation",
        title = "위임",
        summary = "`by lazy`, `observable`, `vetoable`, 클래스 위임 `by`",
        screen = { DelegationDemo() },
    ),
)
