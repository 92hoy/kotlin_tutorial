package com.example.kotlintutorial.ui.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 2차원 좌표. 연산자 오버로딩 예제용 값 객체.
 *
 * Kotlin 포인트: `operator` 키워드를 붙인 함수는 `+`, `-`, `*`, `[]`, `in` 같은 연산자로 호출된다.
 * Java는 연산자 오버로딩이 없어서 `a.add(b)`처럼 메서드를 직접 불러야 함.
 *
 * @property x 가로 값.
 * @property y 세로 값.
 */
data class Vec(val x: Int, val y: Int) {

    /** `a + b`로 호출되는 벡터 덧셈. */
    operator fun plus(other: Vec): Vec = Vec(x + other.x, y + other.y)

    /** `a - b`로 호출되는 벡터 뺄셈. */
    operator fun minus(other: Vec): Vec = Vec(x - other.x, y - other.y)

    /** `a * 3`으로 호출되는 스칼라 곱. */
    operator fun times(k: Int): Vec = Vec(x * k, y * k)

    /** `-a`로 호출되는 부호 반전. */
    operator fun unaryMinus(): Vec = Vec(-x, -y)

    /** `a[0]`, `a[1]`로 성분을 읽는다. 범위를 벗어나면 예외를 던진다. */
    operator fun get(index: Int): Int = when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("Vec 인덱스는 0 또는 1: $index")
    }
}

/**
 * `3 * a`처럼 Int가 왼쪽에 오는 곱셈을 위한 확장 연산자.
 *
 * Kotlin 포인트: 이미 있는 타입(Int)에 확장 함수로 연산자를 추가할 수 있음.
 */
operator fun Int.times(v: Vec): Vec = v * this

/**
 * 정사각형 영역. `in` 연산자 오버로딩 예제용.
 *
 * @property size 한 변의 길이. 원점 기준 `0..size` 범위.
 */
class Area(private val size: Int) {

    /** `point in area`로 호출되며 좌표가 영역 안인지 알려준다. */
    operator fun contains(point: Vec): Boolean = point.x in 0..size && point.y in 0..size
}

/**
 * 비교 연산자 예제용 버전 값. `compareTo` 하나로 `<`, `>`, `<=`, `>=`가 모두 동작한다.
 *
 * @property major 주 버전.
 * @property minor 부 버전.
 */
data class Version(val major: Int, val minor: Int) : Comparable<Version> {

    /** 주 버전, 부 버전 순으로 비교한다. */
    override fun compareTo(other: Version): Int =
        compareValuesBy(this, other, Version::major, Version::minor)
}

/** 연산자 오버로딩 결과 목록을 만든다. */
fun operatorResults(): List<Pair<String, String>> {
    val a = Vec(1, 2)
    val b = Vec(3, 4)
    val area = Area(5)
    val (x, y) = a + b
    return listOf(
        "Vec(1, 2) + Vec(3, 4)" to (a + b).toString(),
        "Vec(3, 4) - Vec(1, 2)" to (b - a).toString(),
        "a * 3" to (a * 3).toString(),
        "3 * a (Int 확장 연산자)" to (3 * a).toString(),
        "-a" to (-a).toString(),
        "a[0], a[1]" to "${a[0]}, ${a[1]}",
        "val (x, y) = a + b" to "x=$x, y=$y",
        "Vec(2, 3) in Area(5)" to (Vec(2, 3) in area).toString(),
        "Vec(9, 3) in Area(5)" to (Vec(9, 3) in area).toString(),
        "Version(1, 2) < Version(1, 10)" to (Version(1, 2) < Version(1, 10)).toString(),
        "Version(2, 0) >= Version(1, 9)" to (Version(2, 0) >= Version(1, 9)).toString(),
    )
}

/** 연산자 오버로딩 데모 화면. */
@Composable
fun OperatorDemo() {
    DemoScaffold("연산자 오버로딩") {
        operatorResults().forEach { (code, result) ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(code, style = MaterialTheme.typography.labelMedium)
                    Text(result, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
