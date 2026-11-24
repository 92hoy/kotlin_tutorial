package com.example.kotlintutorial.ui.tutorial

import androidx.compose.runtime.Composable

/**
 * 튜토리얼 난이도 구분. 홈 화면에서 이 순서대로 묶어서 보여 준다.
 *
 * Kotlin 포인트: `enum class`도 프로퍼티와 생성자를 가질 수 있다.
 * `Level.entries`로 선언 순서대로 전체 값을 얻는다.
 *
 * @property label 화면에 보이는 이름.
 * @property description 이 단계에서 다루는 범위 한 줄 설명.
 */
enum class Level(val label: String, val description: String) {
    BEGINNER("초급", "자주 쓰는 기본 문법"),
    INTERMEDIATE("중급", "타입을 다루는 방식과 비동기 처리"),
    ADVANCED("고급", "스트림, 제네릭, DSL"),
    PROJECT("종합 예제", "배운 기능을 모아 만든 실제 앱"),
}

/**
 * 튜토리얼 기능 한 개의 정보. 홈 목록, 상세 화면, 실습 화면이 모두 이 데이터를 쓴다.
 *
 * Kotlin 포인트: 실습 화면을 `@Composable () -> Unit` 함수 타입 프로퍼티로 들고 있어서
 * 화면 클래스를 따로 상속하거나 등록할 필요가 없다. 새 기능은 이 객체 하나만 추가하면 됨.
 *
 * @property id Navigation 경로에 쓰는 고유 이름.
 * @property title 목록과 상세 화면 제목.
 * @property level 난이도 구분.
 * @property summary 목록에 보이는 한 줄 설명.
 * @property explanation 상세 화면의 설명 본문.
 * @property points 꼭 기억할 핵심 포인트 목록.
 * @property kotlinCode Kotlin 예시 코드.
 * @property javaCode 같은 일을 Java로 쓸 때의 코드. 비교할 게 없으면 null.
 * @property demo 직접 해 볼 수 있는 실습 화면.
 */
data class Topic(
    val id: String,
    val title: String,
    val level: Level,
    val summary: String,
    val explanation: String,
    val points: List<String>,
    val kotlinCode: String,
    val javaCode: String?,
    val demo: @Composable () -> Unit,
)
